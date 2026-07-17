package com.valiukh.protonika.instrumentation

import androidx.lifecycle.ViewModel
import com.valiukh.protonika.instrumentation.usecases.ApplySelectedScriptUseCase
import com.valiukh.protonika.instrumentation.usecases.GetAvailableScriptsUseCases
import com.valiukh.protonika.instrumentation.usecases.GetSelectedScriptUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ViewState(
    val scriptsState: ScriptsState = ScriptsState(),
    val feedbackState: FeedbackState = FeedbackState()
)

data class ScriptsState(
    val availableScriptNames: List<String> = emptyList(),
    val selectedScriptInstructions: List<String> = emptyList(),
)

data class FeedbackState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class InstrumentationSheetViewModel(
    private val getAvailableScriptsUseCases: GetAvailableScriptsUseCases,
    private val getSelectedScriptUseCases: GetSelectedScriptUseCase,
    private val applySelectedScriptUseCase: ApplySelectedScriptUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(value = ViewState())
    val uiState: StateFlow<ViewState> = _uiState.asStateFlow()

    fun init() {
        val availableScripts = getAvailableScriptsUseCases.invoke().getOrNull()
        availableScripts?.let { availableScripts ->
            if(availableScripts.isNotEmpty()) {
                val selectedScriptName = availableScripts.first()
                val selectedScript = getSelectedScriptUseCases.invoke(
                    parameters = GetSelectedScriptUseCase.Params(
                        scriptName = selectedScriptName
                    )
                ).getOrNull()

                _uiState.update { state -> state.copy(
                    scriptsState = ScriptsState(
                        availableScriptNames = availableScripts,
                        selectedScriptInstructions = selectedScript ?: listOf("Unable to show loaded script")
                    )
                ) }
            }
        }
    }

    fun onScriptSelected(position: Int) {
        val selectedScript = getSelectedScriptUseCases.invoke(
            parameters = GetSelectedScriptUseCase.Params(
                scriptName = _uiState.value.scriptsState.availableScriptNames[position]
            )
        ).getOrNull()

        _uiState.update { state -> state.copy(
            scriptsState = state.scriptsState.copy(
                selectedScriptInstructions = selectedScript ?: listOf("Unable to show loaded script")
            )
        ) }
    }

    fun onApplyScript(position: Int) {
        if (_uiState.value.scriptsState.availableScriptNames.isNotEmpty()) {
            val selectedScriptName = _uiState.value.scriptsState.availableScriptNames[position]
            applySelectedScriptUseCase.invoke(
                parameters = ApplySelectedScriptUseCase.Params(
                    scriptName = selectedScriptName
                )
            )
        }
    }

    fun onFeedbackSubmitted(feedback: String) {
        // Feedback backend integration is intentionally deferred; no-op for now.
    }
}