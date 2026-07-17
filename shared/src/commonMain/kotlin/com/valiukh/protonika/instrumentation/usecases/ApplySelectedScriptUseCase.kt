package com.valiukh.protonika.instrumentation.usecases

import com.valiukh.protonika.data.scripts.ScriptsRepository
import com.valiukh.protonika.domain.BaseNonSuspendUseCase

class ApplySelectedScriptUseCase(
    private val scriptsRepository: ScriptsRepository
) : BaseNonSuspendUseCase<ApplySelectedScriptUseCase.Params, Unit>(){

    data class Params(
        val scriptName: String
    )

    override fun execute(parameters: Params) {
        scriptsRepository.setActiveScriptName(parameters.scriptName)
    }
}