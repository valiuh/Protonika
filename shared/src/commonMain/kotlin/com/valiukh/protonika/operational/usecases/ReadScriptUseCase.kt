package com.valiukh.protonika.operational.usecases

import com.valiukh.protonika.data.script.ScriptRepository
import com.valiukh.protonika.data.scripts.ScriptsRepository
import com.valiukh.protonika.domain.BaseUseCase
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name
import kotlinx.coroutines.CoroutineDispatcher

class ReadScriptUseCase(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val scriptRepository: ScriptRepository,
    private val scriptsRepository: ScriptsRepository,
): BaseUseCase<ReadScriptUseCase.Params, String>(coroutineDispatcher) {

    data class Params(
        val file: PlatformFile
    )

    override suspend fun execute(parameters: Params): String {
        val script = scriptRepository.readScript(parameters.file)
        scriptsRepository.addScript(parameters.file.name, script)
        return script
    }
}