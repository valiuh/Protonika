package com.valiukh.protonika.operational

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valiukh.protonika.compose.components.MkOutputConsoleState
import com.valiukh.protonika.compose.components.editor.cursor
import com.valiukh.protonika.compose.components.input.MkButtonType
import com.valiukh.protonika.compose.components.input.MkPrimaryFunctionType
import com.valiukh.protonika.compose.components.input.MkSecondaryFunctionType
import com.valiukh.protonika.compose.components.input.isDigit
import com.valiukh.protonika.compose.components.input.isDot
import com.valiukh.protonika.compose.components.input.isSignToggle
import com.valiukh.protonika.data.script.toInstructions
import com.valiukh.protonika.operational.usecases.GetActiveScriptUseCase
import com.valiukh.protonika.operational.usecases.ReadScriptUseCase
import com.valiukh.protonika.operational.usecases.SaveScriptUseCase
import com.valiukh.protonika.virtualmachine.Mk61
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Describes how the next key press extends the program being typed in [ViewState.instructions].
 *
 * The last element is the "active instruction": the one currently being composed. It always
 * carries the trailing [cursor], which only moves onto a new line once the active instruction is
 * finalized and a new instruction begins.
 */
enum class InputMode {
    /** The active instruction is complete; the next key starts a new instruction. */
    IDLE,

    /** A number is being typed; digits and the decimal point extend the active instruction. */
    NUMBER,

    /** The F (primary) modifier is armed; the active instruction shows a provisional "F". */
    PRIMARY_PENDING,

    /** The K (secondary) modifier is armed; the active instruction shows a provisional "К". */
    SECONDARY_PENDING,

    /** A memory op (X→П / П→X) is the active instruction and is waiting for its register. */
    REGISTER_PENDING,
}

data class ViewState(
    val pressedKeyType: MkButtonType? = null,
    val isPrimaryFunctionActive: Boolean = false,
    val isSecondaryFunctionActive: Boolean = false,
    val outputConsoleState: MkOutputConsoleState = MkOutputConsoleState(),
    val inputMode: InputMode = InputMode.IDLE,
    val instructions: List<String> = listOf(
        cursor
    ),
    val needOpenScriptFile: Boolean = false,
    val needSaveScriptToFile: Boolean = false,
    val needShowInstrumentationPanel: Boolean = false
)

class OperationalConsoleViewModel(
    private val mk61: Mk61,
    private val readScriptUseCase: ReadScriptUseCase,
    private val saveScriptUseCase: SaveScriptUseCase,
    private val getActiveScriptUseCase: GetActiveScriptUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(value = ViewState())
    val uiState: StateFlow<ViewState> = _uiState.asStateFlow()

    private var isRunShortcutArmed: Boolean = false

    fun onKeyPressed(
        pressedKeyType: MkButtonType,
        primaryFunctionType: MkPrimaryFunctionType? = null,
        secondaryFunctionType: MkSecondaryFunctionType? = null
    ) {

        if (_uiState.value.isPrimaryFunctionActive) {
            when (primaryFunctionType) {
                MkPrimaryFunctionType.FUNCTION_F1 -> { openScript(); return }
                MkPrimaryFunctionType.FUNCTION_F2 -> { saveScript(); return }
                MkPrimaryFunctionType.FUNCTION_F3 -> { showInstrumentationPanel(); return }
                MkPrimaryFunctionType.FUNCTION_F4 -> { clearScript(); return }
                MkPrimaryFunctionType.FUNCTION_F5 -> { runScript(); return }
                else -> {}
            }
        }
        _uiState.update { state ->
            state.press(pressedKeyType, primaryFunctionType, secondaryFunctionType)
        }
    }

    /** Called from [OperationalConsoleScreen] after the user selects a script file to open. */
    fun onScriptOpen(file: PlatformFile) {
        viewModelScope.launch {
            readScriptUseCase
                .invoke(ReadScriptUseCase.Params(file))
                .onSuccess { scriptSource ->
                    _uiState.update { state ->
                        state.copy(
                            isPrimaryFunctionActive = false,
                            inputMode = InputMode.IDLE,
                            instructions = scriptSource.toInstructions() + listOf(cursor)
                        )
                    }
                }.onFailure {
                    //TODO: implement error handling connected to ui state and showing error dialog
                }
        }
    }

    /** Called from [OperationalConsoleScreen] after the user selects a destination file to save. */
    fun onScriptSave(file: PlatformFile) {
        viewModelScope.launch {
            val instructions = _uiState.value.instructions
            val script = instructions.toProgramString()
            saveScriptUseCase
                .invoke(SaveScriptUseCase.Params(file, script))
        }
    }

    /** Resets the open-file request once the picker has been shown (selected or cancelled). */
    fun onOpenFilePickerHandled() {
        _uiState.update { state -> state.copy(needOpenScriptFile = false) }
    }

    /** Resets the save-file request once the picker has been shown (selected or cancelled). */
    fun onSaveFilePickerHandled() {
        _uiState.update { state -> state.copy(needSaveScriptToFile = false) }
    }

    /** Resets the instrumentation-panel request once its bottom sheet has been dismissed. */
    fun onInstrumentationPanelDismissed() {
        val scriptSource = getActiveScriptUseCase.invoke().getOrNull()
        val instructions = scriptSource?.toInstructions() ?: listOf(cursor)
        _uiState.update { state ->
            state.copy(
                needShowInstrumentationPanel = false,
                inputMode = InputMode.IDLE,
                instructions = instructions
            )
        }
    }

    private fun openScript() {
        _uiState.update { state ->
            state.copy(
                needOpenScriptFile = true,
                isPrimaryFunctionActive = false,
                inputMode = InputMode.IDLE,
                instructions = state.instructions.dropLast(1) + listOf(cursor),
            )
        }
    }

    private fun saveScript() {
        _uiState.update { state ->
            state.copy(
                needSaveScriptToFile = true,
                isPrimaryFunctionActive = false,
                inputMode = InputMode.IDLE,
                instructions = state.instructions.dropLast(1) + listOf(cursor),
            )
        }
    }

    private fun showInstrumentationPanel() {
        _uiState.update { state ->
            state.copy(
                needShowInstrumentationPanel = true,
                isPrimaryFunctionActive = false,
                inputMode = InputMode.IDLE,
                instructions = state.instructions.dropLast(1) + listOf(cursor),
            )
        }
    }

    private fun clearScript() {
        isRunShortcutArmed = false
        _uiState.update { state ->
            state.copy(
                pressedKeyType = null,
                isPrimaryFunctionActive = false,
                isSecondaryFunctionActive = false,
                inputMode = InputMode.IDLE,
                instructions = listOf(cursor),
            )
        }
    }

    private fun runScript() {
        isRunShortcutArmed = true
        _uiState.update { state ->
            state.copy(
                isPrimaryFunctionActive = false,
                inputMode = InputMode.IDLE,
                instructions = state.instructions.dropLast(1) + listOf(cursor),
            )
        }

        val instructions = _uiState.value.instructions
        val script = instructions.toProgramString()
        mk61.uploadProgram(script)
        mk61.calculate()

        val stackRegisters = mk61.printRegisters()
        val X = stackRegisters[0]
        val Y = stackRegisters[1]
        val Z = stackRegisters[2]
        val T = stackRegisters[3]

        val register0 = mk61.printDataRegister("0")
        val register1 = mk61.printDataRegister("1")
        val register2 = mk61.printDataRegister("2")
        val register3 = mk61.printDataRegister("3")
        val register4 = mk61.printDataRegister("4")
        val register5 = mk61.printDataRegister("5")
        val register6 = mk61.printDataRegister("6")
        val register7 = mk61.printDataRegister("7")
        val register8 = mk61.printDataRegister("8")
        val register9 = mk61.printDataRegister("9")
        val registerA = mk61.printDataRegister("a")
        val registerB = mk61.printDataRegister("b")
        val registerC = mk61.printDataRegister("c")
        val registerD = mk61.printDataRegister("d")
        val registerE = mk61.printDataRegister("e")

        val outputConsoleState = MkOutputConsoleState(
            registerX = X,
            registerY = Y,
            registerZ = Z,
            registerT = T,
            registerX1 = mk61.printX1(),
            register0 = register0 ?: 0.0,
            register1 = register1 ?: 0.0,
            register2 = register2 ?: 0.0,
            register3 = register3 ?: 0.0,
            register4 = register4 ?: 0.0,
            register5 = register5 ?: 0.0,
            register6 = register6 ?: 0.0,
            register7 = register7 ?: 0.0,
            register8 = register8 ?: 0.0,
            register9 = register9 ?: 0.0,
            registerA = registerA ?: 0.0,
            registerB = registerB ?: 0.0,
            registerC = registerC ?: 0.0,
            registerD = registerD ?: 0.0,
            registerE = registerE ?: 0.0,
        )

        _uiState.update {
            it.copy(
                outputConsoleState = outputConsoleState,
            )
        }
        isRunShortcutArmed = false
    }
}

private const val PROVISIONAL_PRIMARY = "F"
private const val PROVISIONAL_SECONDARY = "К"

private val REGISTER_LETTERS = setOf(
    MkButtonType.BUTTON_A,
    MkButtonType.BUTTON_B,
    MkButtonType.BUTTON_C,
    MkButtonType.BUTTON_D,
    MkButtonType.BUTTON_E,
)

private fun ViewState.press(
    type: MkButtonType,
    primaryType: MkPrimaryFunctionType?,
    secondaryType: MkSecondaryFunctionType?,
): ViewState = when (inputMode) {
    InputMode.PRIMARY_PENDING -> pressWhilePrimaryPending(type, primaryType, secondaryType)
    InputMode.SECONDARY_PENDING -> pressWhileSecondaryPending(type, primaryType, secondaryType)
    InputMode.REGISTER_PENDING -> pressWhileRegisterPending(type)
    InputMode.IDLE, InputMode.NUMBER -> pressNormal(type, primaryType, secondaryType)
}

/**
 * Handles a key press with no armed modifier: digit accumulation, number decorations,
 * memory ops (which then await a register), and own-token commands.
 */
private fun ViewState.pressNormal(
    type: MkButtonType,
    primaryType: MkPrimaryFunctionType?,
    secondaryType: MkSecondaryFunctionType?,
): ViewState {
    if (type == MkButtonType.BUTTON_F) {
        return copy(
            instructions = instructions.startInstruction(PROVISIONAL_PRIMARY),
            inputMode = InputMode.PRIMARY_PENDING,
            isPrimaryFunctionActive = true,
            isSecondaryFunctionActive = false,
            pressedKeyType = type,
        )
    }
    if (type == MkButtonType.BUTTON_K) {
        return copy(
            instructions = instructions.startInstruction(PROVISIONAL_SECONDARY),
            inputMode = InputMode.SECONDARY_PENDING,
            isPrimaryFunctionActive = false,
            isSecondaryFunctionActive = true,
            pressedKeyType = type,
        )
    }
    if (type.isDigit()) {
        val updated = if (inputMode == InputMode.NUMBER) {
            instructions.appendActive(type.text)
        } else {
            instructions.startInstruction(type.text)
        }
        return copy(instructions = updated, inputMode = InputMode.NUMBER, pressedKeyType = type)
    }
    if (type.isDot()) {
        val updated = when {
            inputMode != InputMode.NUMBER -> instructions.startInstruction("0.")
            instructions.activeText().contains('.') -> instructions
            else -> instructions.appendActive(".")
        }
        return copy(instructions = updated, inputMode = InputMode.NUMBER, pressedKeyType = type)
    }
    if (type.isSignToggle()) {
        if (inputMode != InputMode.NUMBER) return copy(pressedKeyType = type)
        val current = instructions.activeText()
        if (current.isEmpty()) return copy(pressedKeyType = type)
        val toggled = if (current.startsWith("-")) current.drop(1) else "-$current"
        return copy(instructions = instructions.setActive(toggled), pressedKeyType = type)
    }
    // Register letters are only meaningful as a memory-op argument (handled in REGISTER_PENDING).
    if (type in REGISTER_LETTERS) return copy(pressedKeyType = type)

    // Memory ops emit their token and then wait for the register argument to merge in.
    if (type == MkButtonType.BUTTON_X_TO_REGISTER || type == MkButtonType.BUTTON_REGISTER_TO_X) {
        val token = type.toCommand()?.commandMnemonics ?: return copy(pressedKeyType = type)
        return copy(
            instructions = instructions.startInstruction(token),
            inputMode = InputMode.REGISTER_PENDING,
            pressedKeyType = type,
        )
    }

    // Any other own-token command (arithmetic, stack, jumps, С/П, В/O, СX, …). Keys without a
    // VM equivalent (ВП, →ШГ, ←ШГ) map to null and only update the pressed-key highlight.
    val command = type.toCommand() ?: return copy(pressedKeyType = type)
    return copy(
        instructions = instructions.startInstruction(command.commandMnemonics),
        inputMode = InputMode.IDLE,
        pressedKeyType = type,
    )
}

/**
 * Handles a key press while the F (primary) modifier is armed. Resolves the yellow function,
 * or swaps/cancels the modifier.
 */
private fun ViewState.pressWhilePrimaryPending(
    type: MkButtonType,
    primaryType: MkPrimaryFunctionType?,
    secondaryType: MkSecondaryFunctionType?,
): ViewState {
    // Pressing F again cancels the armed modifier.
    if (type == MkButtonType.BUTTON_F) {
        return copy(
            instructions = instructions.clearActive(),
            inputMode = InputMode.IDLE,
            isPrimaryFunctionActive = false,
            pressedKeyType = type,
        )
    }
    // F followed by K swaps to the secondary modifier.
    if (type == MkButtonType.BUTTON_K) {
        return copy(
            instructions = instructions.setActive(PROVISIONAL_SECONDARY),
            inputMode = InputMode.SECONDARY_PENDING,
            isPrimaryFunctionActive = false,
            isSecondaryFunctionActive = true,
            pressedKeyType = type,
        )
    }
    val command = primaryType?.toCommand()
    if (command != null) {
        return copy(
            instructions = instructions.setActive(command.commandMnemonics),
            inputMode = InputMode.IDLE,
            isPrimaryFunctionActive = false,
            pressedKeyType = type,
        )
    }
    // A recognised F-function without a VM equivalent (АВТ, ПРГ, СF): drop the provisional F.
    if (primaryType != null) {
        return copy(
            instructions = instructions.clearActive(),
            inputMode = InputMode.IDLE,
            isPrimaryFunctionActive = false,
            pressedKeyType = type,
        )
    }
    // The key has no F-function at all: cancel F and process the key on its own.
    return copy(
        instructions = instructions.clearActive(),
        inputMode = InputMode.IDLE,
        isPrimaryFunctionActive = false,
    ).pressNormal(type, primaryType, secondaryType)
}

/**
 * Handles a key press while the K (secondary) modifier is armed. Indirect memory/jump ops keep
 * the "К" prefix; blue math functions replace it; the modifier can also be swapped or cancelled.
 */
private fun ViewState.pressWhileSecondaryPending(
    type: MkButtonType,
    primaryType: MkPrimaryFunctionType?,
    secondaryType: MkSecondaryFunctionType?,
): ViewState {
    // Pressing K again cancels the armed modifier.
    if (type == MkButtonType.BUTTON_K) {
        return copy(
            instructions = instructions.clearActive(),
            inputMode = InputMode.IDLE,
            isSecondaryFunctionActive = false,
            pressedKeyType = type,
        )
    }
    // K followed by F swaps to the primary modifier.
    if (type == MkButtonType.BUTTON_F) {
        return copy(
            instructions = instructions.setActive(PROVISIONAL_PRIMARY),
            inputMode = InputMode.PRIMARY_PENDING,
            isPrimaryFunctionActive = true,
            isSecondaryFunctionActive = false,
            pressedKeyType = type,
        )
    }
    // Indirect memory / jump ops keep the "К" prefix (e.g. К X→П, К БП).
    val indirect = type.toIndirectCommand()
    if (indirect != null) {
        val mode =
            if (type == MkButtonType.BUTTON_X_TO_REGISTER || type == MkButtonType.BUTTON_REGISTER_TO_X) {
                InputMode.REGISTER_PENDING
            } else {
                InputMode.IDLE
            }
        return copy(
            instructions = instructions.setActive(indirect.commandMnemonics),
            inputMode = mode,
            isSecondaryFunctionActive = false,
            pressedKeyType = type,
        )
    }
    // Indirect conditional jumps keep the "К" prefix (e.g. К X=0).
    val indirectConditional = primaryType?.toIndirectCommand()
    if (indirectConditional != null) {
        return copy(
            instructions = instructions.setActive(indirectConditional.commandMnemonics),
            inputMode = InputMode.IDLE,
            isSecondaryFunctionActive = false,
            pressedKeyType = type,
        )
    }
    // Blue math functions drop the "К" prefix (e.g. [x], max, ∧, °←', СЧ). Any function without
    // a VM equivalent would just drop the provisional К.
    if (secondaryType != null) {
        val command = secondaryType.toCommand()
        return if (command != null) {
            copy(
                instructions = instructions.setActive(command.commandMnemonics),
                inputMode = InputMode.IDLE,
                isSecondaryFunctionActive = false,
                pressedKeyType = type,
            )
        } else {
            copy(
                instructions = instructions.clearActive(),
                inputMode = InputMode.IDLE,
                isSecondaryFunctionActive = false,
                pressedKeyType = type,
            )
        }
    }
    // The key has no K-function at all: cancel K and process the key on its own.
    return copy(
        instructions = instructions.clearActive(),
        inputMode = InputMode.IDLE,
        isSecondaryFunctionActive = false,
    ).pressNormal(type, primaryType, secondaryType)
}

/**
 * Handles the register argument that follows a memory op, merging it into the op token
 * (e.g. `X→П` + `8` -> `X→П 8`). Non-register keys are ignored while waiting.
 */
private fun ViewState.pressWhileRegisterPending(type: MkButtonType): ViewState {
    val isRegister = type.isDigit() || type in REGISTER_LETTERS
    if (!isRegister) return copy(pressedKeyType = type)
    return copy(
        instructions = instructions.appendActive(" ${type.text}"),
        inputMode = InputMode.IDLE,
        pressedKeyType = type,
    )
}

/** The active instruction's text (the last element) without its trailing [cursor]. */
private fun List<String>.activeText(): String = last().removeSuffix(cursor)

/** Replaces the active instruction's content, keeping the [cursor] at its end. */
private fun List<String>.setActive(content: String): List<String> =
    toMutableList().apply { this[lastIndex] = content + cursor }

/** Appends [suffix] to the active instruction, keeping the [cursor] at its end. */
private fun List<String>.appendActive(suffix: String): List<String> = setActive(activeText() + suffix)

/** Clears the active instruction back to an empty caret line. */
private fun List<String>.clearActive(): List<String> = setActive("")

/**
 * Finalizes the active instruction and begins a new one holding [content] and the [cursor].
 * Reuses the active line while it is still empty, so no blank line is left behind.
 */
private fun List<String>.startInstruction(content: String): List<String> {
    val active = activeText()
    if (active.isEmpty()) return setActive(content)
    return toMutableList().apply {
        this[lastIndex] = active
        add(content + cursor)
    }
}

/**
 * Renders the instruction list as a printable program string: drops the last
 * entry (the active line carrying the cursor) and joins the remaining
 * non-blank instructions with newlines.
 */
fun List<String>.toProgramString(): String =
    dropLast(1)
        .filter { it.isNotBlank() }
        .joinToString(separator = "\n")
