package com.valiukh.protonika.operational.usecases

import com.valiukh.protonika.data.script.ScriptRepository
import com.valiukh.protonika.domain.BaseUseCase
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.CoroutineDispatcher

class SaveScriptUseCase(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val scriptRepository: ScriptRepository,
): BaseUseCase<SaveScriptUseCase.Params, Unit>(coroutineDispatcher) {

    data class Params(
        val file: PlatformFile,
        val script: String
    )

    override suspend fun execute(parameters: Params) {
        scriptRepository.saveScript(parameters.file, parameters.script)
    }
}