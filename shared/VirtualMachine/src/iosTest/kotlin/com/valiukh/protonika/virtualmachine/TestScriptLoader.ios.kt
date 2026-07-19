package com.valiukh.protonika.virtualmachine

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned
import platform.Foundation.NSBundle
import platform.posix.SEEK_END
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.rewind

private const val TEST_SCRIPTS_DIR = "test_scripts"

internal actual fun loadTestScript(scriptName: String): String {
    val dotIndex = scriptName.lastIndexOf('.')
    val resourceName = if (dotIndex > 0) scriptName.substring(0, dotIndex) else scriptName
    val resourceExt = if (dotIndex > 0) scriptName.substring(dotIndex + 1) else null

    val path = NSBundle.mainBundle.pathForResource(
        name = resourceName,
        ofType = resourceExt,
        inDirectory = TEST_SCRIPTS_DIR,
    ) ?: error("Test script not found in bundle: $TEST_SCRIPTS_DIR/$scriptName")

    return readUtf8File(path)
}

@OptIn(ExperimentalForeignApi::class)
private fun readUtf8File(path: String): String {
    val file = fopen(path, "rb") ?: error("Unable to open test script file: $path")
    try {
        check(fseek(file, 0, SEEK_END) == 0) { "Unable to seek test script file: $path" }
        val fileSize = ftell(file)
        check(fileSize >= 0) { "Unable to get test script size: $path" }
        rewind(file)

        if (fileSize == 0L) {
            return ""
        }

        val size = fileSize.toInt()
        val bytes = ByteArray(size)
        val bytesRead = bytes.usePinned { pinned ->
            fread(pinned.addressOf(0), 1.convert(), size.convert(), file).toInt()
        }
        check(bytesRead == size) { "Unable to fully read test script file: $path" }

        return bytes.decodeToString()
    } finally {
        fclose(file)
    }
}
