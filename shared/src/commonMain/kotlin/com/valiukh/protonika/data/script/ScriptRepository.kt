package com.valiukh.protonika.data.script

import io.github.vinceglb.filekit.PlatformFile

interface ScriptRepository {

    suspend fun readScript(file: PlatformFile): String

    suspend fun saveScript(file: PlatformFile, script: String)

}

fun String.toInstructions(): List<String> {
    if (this.isEmpty()) return emptyList()
    val instructions: MutableList<String> = mutableListOf()
    this
        .split("\n", ";")
        .forEach { instructions.add(it.trim()) }
    return instructions
}