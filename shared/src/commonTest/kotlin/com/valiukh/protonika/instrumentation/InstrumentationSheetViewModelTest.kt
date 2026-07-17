package com.valiukh.protonika.instrumentation

import com.valiukh.protonika.data.script.toInstructions
import com.valiukh.protonika.data.scripts.ScriptsRepository
import com.valiukh.protonika.instrumentation.usecases.ApplySelectedScriptUseCase
import com.valiukh.protonika.instrumentation.usecases.GetAvailableScriptsUseCases
import com.valiukh.protonika.instrumentation.usecases.GetSelectedScriptUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class FakeScriptsRepository(
    private val scripts: Map<String, String> = emptyMap(),
) : ScriptsRepository {
    private var activeScriptName: String = ""

    override fun addScript(name: String, script: String) = Unit

    override fun getScript(name: String): String =
        scripts[name] ?: throw IllegalArgumentException("Script not found")

    override fun getAllScriptsNames(): List<String> = scripts.keys.toList()

    override fun getAllScripts(): Map<String, String> = scripts

    override fun getActiveScriptName(): String = activeScriptName

    override fun setActiveScriptName(scriptName: String) {
        activeScriptName = scriptName
    }
}

/** Reports names but always fails to load a script body, exercising the fallback branch. */
private class FailingScriptsRepository(
    private val names: List<String>,
) : ScriptsRepository {
    private var activeScriptName: String = ""

    override fun addScript(name: String, script: String) = Unit

    override fun getScript(name: String): String =
        throw IllegalArgumentException("Unable to read $name")

    override fun getAllScriptsNames(): List<String> = names

    override fun getAllScripts(): Map<String, String> = emptyMap()

    override fun getActiveScriptName(): String = activeScriptName

    override fun setActiveScriptName(scriptName: String) {
        activeScriptName = scriptName
    }
}

class InstrumentationSheetViewModelTest {

    private fun viewModel(repository: ScriptsRepository): InstrumentationSheetViewModel =
        InstrumentationSheetViewModel(
            getAvailableScriptsUseCases = GetAvailableScriptsUseCases(repository),
            getSelectedScriptUseCases = GetSelectedScriptUseCase(repository),
            applySelectedScriptUseCase = ApplySelectedScriptUseCase(repository),
        )

    @Test
    fun `init loads available scripts and selects the first one`() {
        val vm = viewModel(
            FakeScriptsRepository(
                mapOf(
                    "a.mk61" to "15\nX→П 8",
                    "b.mk61" to "12",
                ),
            ),
        )
        vm.init()

        val state = vm.uiState.value.scriptsState
        assertEquals(listOf("a.mk61", "b.mk61"), state.availableScriptNames)
        assertEquals("15\nX→П 8".toInstructions(), state.selectedScriptInstructions)
    }

    @Test
    fun `init with no scripts leaves the state empty`() {
        val vm = viewModel(FakeScriptsRepository(emptyMap()))
        vm.init()

        val state = vm.uiState.value.scriptsState
        assertTrue(state.availableScriptNames.isEmpty())
        assertTrue(state.selectedScriptInstructions.isEmpty())
    }

    @Test
    fun `init falls back to an error message when the script cannot be loaded`() {
        val vm = viewModel(FailingScriptsRepository(listOf("a.mk61")))
        vm.init()

        val state = vm.uiState.value.scriptsState
        assertEquals(listOf("a.mk61"), state.availableScriptNames)
        assertEquals(listOf("Unable to show loaded script"), state.selectedScriptInstructions)
    }

    @Test
    fun `onScriptSelected loads the instructions for the chosen script`() {
        val vm = viewModel(
            FakeScriptsRepository(
                mapOf(
                    "a.mk61" to "1",
                    "b.mk61" to "2\n3",
                ),
            ),
        )
        vm.init()

        vm.onScriptSelected(position = 1)

        assertEquals(
            "2\n3".toInstructions(),
            vm.uiState.value.scriptsState.selectedScriptInstructions,
        )
    }
}
