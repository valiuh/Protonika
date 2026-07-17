package com.valiukh.protonika.instrumentation.usecases

import com.valiukh.protonika.data.scripts.ScriptsRepository
import com.valiukh.protonika.domain.BaseNonSuspendParamsLessUseCase

class GetAvailableScriptsUseCases(
    private val scriptsRepository: ScriptsRepository
): BaseNonSuspendParamsLessUseCase<List<String>>() {

    override fun execute(): List<String> {
        return scriptsRepository.getAllScriptsNames()
    }
}