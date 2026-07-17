package com.valiukh.protonika.domain.script

import com.valiukh.protonika.data.script.ScriptRepository
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString

/**
 * [com.valiukh.protonika.data.script.ScriptRepository] backed by FileKit's [io.github.vinceglb.filekit.PlatformFile], so reading and writing work the same way
 * on Android and iOS regardless of whether the file is a real path, an Android `content://` URI or
 * an iOS security-scoped URL.
 */
class ScriptRepositoryImpl : ScriptRepository {

    override suspend fun readScript(file: PlatformFile): String =
        file.readString()

    override suspend fun saveScript(file: PlatformFile, script: String) {
        file.writeString(script)
    }
}