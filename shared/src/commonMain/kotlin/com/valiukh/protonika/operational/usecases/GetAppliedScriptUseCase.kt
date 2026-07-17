package com.valiukh.protonika.operational.usecases

import com.valiukh.protonika.data.scripts.ScriptsRepository
import com.valiukh.protonika.domain.BaseNonSuspendParamsLessUseCase

class GetActiveScriptUseCase(
    private val scriptsRepository: ScriptsRepository
) : BaseNonSuspendParamsLessUseCase<String>() {

    override fun execute(): String {
        val activeScriptName = scriptsRepository.getActiveScriptName()
        return scriptsRepository.getScript(activeScriptName)
    }
}