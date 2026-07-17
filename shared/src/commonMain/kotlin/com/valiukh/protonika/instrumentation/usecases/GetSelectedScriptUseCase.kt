package com.valiukh.protonika.instrumentation.usecases

import com.valiukh.protonika.data.script.toInstructions
import com.valiukh.protonika.data.scripts.ScriptsRepository
import com.valiukh.protonika.domain.BaseNonSuspendUseCase

class GetSelectedScriptUseCase(
    private val scriptsRepository: ScriptsRepository
): BaseNonSuspendUseCase<GetSelectedScriptUseCase.Params, List<String>>() {

    data class Params(
        val scriptName: String
    )

    override fun execute(parameters: Params): List<String> {
        return scriptsRepository
            .getScript(parameters.scriptName)
            .toInstructions()
    }
}