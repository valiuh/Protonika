package com.valiukh.protonika.minibasic.translator

private const val TEST_SCRIPTS_DIR = "test_scripts"

internal actual fun loadTestScript(scriptName: String): String {
    val path = "$TEST_SCRIPTS_DIR/$scriptName"
    val stream = Thread.currentThread().contextClassLoader?.getResourceAsStream(path)
        ?: TranslatorTest::class.java.classLoader?.getResourceAsStream(path)
        ?: error("Test script not found: $path")

    return stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
}
