package com.valiukh.protonika.operational_console

import com.valiukh.protonika.compose.components.editor.cursor
import com.valiukh.protonika.compose.components.input.Key
import com.valiukh.protonika.compose.components.input.MkButtonType
import com.valiukh.protonika.compose.components.input.buttons
import com.valiukh.protonika.data.script.ScriptRepository
import com.valiukh.protonika.domain.scripts.ScriptsRepositoryImpl
import com.valiukh.protonika.operational.usecases.GetActiveScriptUseCase
import com.valiukh.protonika.operational.OperationalConsoleViewModel
import com.valiukh.protonika.operational.usecases.ReadScriptUseCase
import com.valiukh.protonika.operational.usecases.SaveScriptUseCase
import com.valiukh.protonika.virtualmachine.Mk61
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class FakeScriptRepository : ScriptRepository {
    var savedScript: String? = null
    var scriptToReturn: String = ""

    override suspend fun readScript(file: PlatformFile): String = scriptToReturn

    override suspend fun saveScript(file: PlatformFile, script: String) {
        savedScript = script
    }
}

class OperationalConsoleViewModelTest {

    private val keyByType: Map<MkButtonType, Key> = buttons.flatten().associateBy { it.type }

    private fun viewModel(): OperationalConsoleViewModel {
        val scriptRepository = FakeScriptRepository()
        val scriptsRepository = ScriptsRepositoryImpl()
        return OperationalConsoleViewModel(
            mk61 = Mk61(),
            readScriptUseCase = ReadScriptUseCase(
                coroutineDispatcher = Dispatchers.Unconfined,
                scriptRepository = scriptRepository,
                scriptsRepository = scriptsRepository,
            ),
            saveScriptUseCase = SaveScriptUseCase(
                coroutineDispatcher = Dispatchers.Unconfined,
                scriptRepository = scriptRepository,
            ),
            getActiveScriptUseCase = GetActiveScriptUseCase(
                scriptsRepository = scriptsRepository,
            ),
        )
    }

    /** Presses a key, supplying the same primary/secondary labels the UI surface would. */
    private fun OperationalConsoleViewModel.press(type: MkButtonType) {
        val key = keyByType.getValue(type)
        onKeyPressed(type, key.primaryType, key.secondaryType)
    }

    private fun OperationalConsoleViewModel.press(vararg types: MkButtonType) {
        types.forEach { press(it) }
    }

    private val OperationalConsoleViewModel.instructions: List<String>
        get() = uiState.value.instructions

    // The active (last) instruction always carries the embedded cursor; earlier ones are committed.
    private fun program(vararg tokens: String): List<String> =
        if (tokens.isEmpty()) listOf(cursor) else tokens.dropLast(1) + (tokens.last() + cursor)

    // region number entry

    @Test
    fun `consecutive digits accumulate into a single element`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_1, MkButtonType.BUTTON_2)

        assertEquals(program("12"), vm.instructions)
    }

    @Test
    fun `an operation commits the current number and starts a new token`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_1, MkButtonType.BUTTON_2, MkButtonType.BUTTON_PLUS, MkButtonType.BUTTON_3)

        assertEquals(program("12", "+", "3"), vm.instructions)
    }

    @Test
    fun `decimal separator extends the current number and is entered only once`() {
        val vm = viewModel()

        vm.press(
            MkButtonType.BUTTON_1,
            MkButtonType.BUTTON_DECIMAL_SEPARATOR,
            MkButtonType.BUTTON_2,
            MkButtonType.BUTTON_DECIMAL_SEPARATOR,
        )

        assertEquals(program("1.2"), vm.instructions)
    }

    @Test
    fun `decimal separator on an empty token starts a leading zero`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_DECIMAL_SEPARATOR, MkButtonType.BUTTON_5)

        assertEquals(program("0.5"), vm.instructions)
    }

    @Test
    fun `sign toggle flips the sign of the current number`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_5, MkButtonType.BUTTON_SIGN_TOGGLE)
        assertEquals(program("-5"), vm.instructions)

        vm.press(MkButtonType.BUTTON_SIGN_TOGGLE)
        assertEquals(program("5"), vm.instructions)
    }

    // endregion

    // region memory ops and jumps

    @Test
    fun `memory op merges its register argument into one token`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_8)

        assertEquals(program("X→П 8"), vm.instructions)
    }

    @Test
    fun `memory op merges a letter register argument`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_D)

        assertEquals(program("X→П d"), vm.instructions)
    }

    @Test
    fun `store merges every letter register argument`() {
        val letters = listOf(
            MkButtonType.BUTTON_A to "a",
            MkButtonType.BUTTON_B to "b",
            MkButtonType.BUTTON_C to "c",
            MkButtonType.BUTTON_D to "d",
            MkButtonType.BUTTON_E to "e",
        )
        for ((key, name) in letters) {
            val vm = viewModel()

            vm.press(MkButtonType.BUTTON_X_TO_REGISTER, key)

            assertEquals(program("X→П $name"), vm.instructions)
        }
    }

    @Test
    fun `recall merges every letter register argument`() {
        val letters = listOf(
            MkButtonType.BUTTON_A to "a",
            MkButtonType.BUTTON_B to "b",
            MkButtonType.BUTTON_C to "c",
            MkButtonType.BUTTON_D to "d",
            MkButtonType.BUTTON_E to "e",
        )
        for ((key, name) in letters) {
            val vm = viewModel()

            vm.press(MkButtonType.BUTTON_REGISTER_TO_X, key)

            assertEquals(program("П→X $name"), vm.instructions)
        }
    }

    @Test
    fun `indirect memory op merges a letter register argument`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_E)

        assertEquals(program("К X→П e"), vm.instructions)
    }

    @Test
    fun `F followed by a letter key triggers its script shortcut without emitting a token`() {
        val vm = viewModel()

        // F + a is the Ф1 (open script) shortcut: it must not leave an "F" or "a" token behind.
        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_A)

        assertEquals(program(), vm.instructions)
        assertFalse(vm.uiState.value.isPrimaryFunctionActive)
    }

    @Test
    fun `F followed by C requests the instrumentation panel without emitting a token`() {
        val vm = viewModel()

        // F + c is the Ф3 (instrumentation) shortcut.
        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_C)

        assertTrue(vm.uiState.value.needShowInstrumentationPanel)
        assertFalse(vm.uiState.value.isPrimaryFunctionActive)
        assertEquals(program(), vm.instructions)
    }

    @Test
    fun `dismissing the instrumentation panel clears the request`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_C)
        vm.onInstrumentationPanelDismissed()

        assertFalse(vm.uiState.value.needShowInstrumentationPanel)
    }

    @Test
    fun `jump keeps its address as a separate token`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_GOTO, MkButtonType.BUTTON_1, MkButtonType.BUTTON_0)

        assertEquals(program("БП", "10"), vm.instructions)
    }

    // endregion

    // region F (primary) resolution

    @Test
    fun `F resolves a plain function and drops the F`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_7)

        assertEquals(program("sin"), vm.instructions)
        assertFalse(vm.uiState.value.isPrimaryFunctionActive)
    }

    @Test
    fun `F resolves a conditional jump`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_STEP_BACK)

        assertEquals(program("X=0"), vm.instructions)
    }

    @Test
    fun `F on a memory key resolves to the loop command`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_REGISTER_TO_X)

        assertEquals(program("L0"), vm.instructions)
    }

    @Test
    fun `F resolves the last-x function`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_ENTER)

        assertEquals(program("Вx"), vm.instructions)
    }

    // endregion

    // region K (secondary) resolution

    @Test
    fun `K keeps its prefix for an indirect memory op and merges the register`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_8)

        assertEquals(program("К X→П 8"), vm.instructions)
    }

    @Test
    fun `K keeps its prefix for an indirect conditional jump`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_STEP_BACK)

        assertEquals(program("К X=0"), vm.instructions)
    }

    @Test
    fun `K drops its prefix for a blue math function`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_7)

        assertEquals(program("[x]"), vm.instructions)
        assertFalse(vm.uiState.value.isSecondaryFunctionActive)
    }

    @Test
    fun `K resolves the angle conversions dropping its prefix`() {
        val hmToDeg = viewModel()
        hmToDeg.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_6)
        assertEquals(program("°←'"), hmToDeg.instructions)

        val degToHm = viewModel()
        degToHm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_PLUS)
        assertEquals(program("°→'"), degToHm.instructions)

        val hmsToDeg = viewModel()
        hmsToDeg.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_3)
        assertEquals(program("°←''\""), hmsToDeg.instructions)

        val degToHms = viewModel()
        degToHms.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_SWAP)
        assertEquals(program("°→''\""), degToHms.instructions)
    }

    @Test
    fun `K resolves the random function dropping its prefix`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_ENTER)

        assertEquals(program("СЧ"), vm.instructions)
    }

    // endregion

    // region modifier swapping and cancelling

    @Test
    fun `pressing K after F swaps the armed modifier`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_K)

        assertEquals(program("К"), vm.instructions)
        assertTrue(vm.uiState.value.isSecondaryFunctionActive)
        assertFalse(vm.uiState.value.isPrimaryFunctionActive)
    }

    @Test
    fun `pressing F after K swaps the armed modifier`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_F)

        assertEquals(program("F"), vm.instructions)
        assertTrue(vm.uiState.value.isPrimaryFunctionActive)
        assertFalse(vm.uiState.value.isSecondaryFunctionActive)
    }

    @Test
    fun `pressing the armed modifier again cancels it`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_F)

        assertEquals(program(), vm.instructions)
        assertFalse(vm.uiState.value.isPrimaryFunctionActive)
    }

    // endregion

    // region full programs reproducing .mk61 scripts

    @Test
    fun `reproduces the X to register script`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_1, MkButtonType.BUTTON_5)
        vm.press(MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_8)
        vm.press(MkButtonType.BUTTON_1, MkButtonType.BUTTON_2)
        vm.press(MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_D)
        vm.press(MkButtonType.BUTTON_PAUSE)

        assertEquals(program("15", "X→П 8", "12", "X→П d", "С/П"), vm.instructions)
    }

    @Test
    fun `reproduces the indirect K X to register script`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_1)
        vm.press(MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_8)
        vm.press(MkButtonType.BUTTON_1, MkButtonType.BUTTON_0)
        vm.press(MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_D)
        vm.press(MkButtonType.BUTTON_1, MkButtonType.BUTTON_5)
        vm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_8)
        vm.press(MkButtonType.BUTTON_1, MkButtonType.BUTTON_2)
        vm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_D)
        vm.press(MkButtonType.BUTTON_PAUSE)

        assertEquals(
            program("1", "X→П 8", "10", "X→П d", "15", "К X→П 8", "12", "К X→П d", "С/П"),
            vm.instructions,
        )
    }

    @Test
    fun `reproduces the conditional jump script`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_2)
        vm.press(MkButtonType.BUTTON_ENTER)
        vm.press(MkButtonType.BUTTON_3)
        vm.press(MkButtonType.BUTTON_PLUS)
        vm.press(MkButtonType.BUTTON_F, MkButtonType.BUTTON_STEP_BACK)
        vm.press(MkButtonType.BUTTON_1, MkButtonType.BUTTON_0)
        vm.press(MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_8)
        vm.press(MkButtonType.BUTTON_ENTER)
        vm.press(MkButtonType.BUTTON_6)
        vm.press(MkButtonType.BUTTON_MINUS)
        vm.press(MkButtonType.BUTTON_ENTER)
        vm.press(MkButtonType.BUTTON_2)
        vm.press(MkButtonType.BUTTON_PLUS)
        vm.press(MkButtonType.BUTTON_PAUSE)

        assertEquals(
            program("2", "B↑", "3", "+", "X=0", "10", "X→П 8", "B↑", "6", "-", "B↑", "2", "+", "С/П"),
            vm.instructions,
        )
    }

    @Test
    fun `reproduces the indirect goto script`() {
        val vm = viewModel()

        vm.press(MkButtonType.BUTTON_1, MkButtonType.BUTTON_3)
        vm.press(MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_0)
        vm.press(MkButtonType.BUTTON_2)
        vm.press(MkButtonType.BUTTON_ENTER)
        vm.press(MkButtonType.BUTTON_3)
        vm.press(MkButtonType.BUTTON_MULTIPLY)
        vm.press(MkButtonType.BUTTON_K, MkButtonType.BUTTON_GOTO)
        vm.press(MkButtonType.BUTTON_0)
        vm.press(MkButtonType.BUTTON_X_TO_REGISTER, MkButtonType.BUTTON_8)
        vm.press(MkButtonType.BUTTON_ENTER)
        vm.press(MkButtonType.BUTTON_6)
        vm.press(MkButtonType.BUTTON_MINUS)
        vm.press(MkButtonType.BUTTON_ENTER)
        vm.press(MkButtonType.BUTTON_2)
        vm.press(MkButtonType.BUTTON_PLUS)
        vm.press(MkButtonType.BUTTON_PAUSE)

        assertEquals(
            program(
                "13", "X→П 0", "2", "B↑", "3", "×", "К БП", "0", "X→П 8",
                "B↑", "6", "-", "B↑", "2", "+", "С/П",
            ),
            vm.instructions,
        )
    }

    // endregion
}
