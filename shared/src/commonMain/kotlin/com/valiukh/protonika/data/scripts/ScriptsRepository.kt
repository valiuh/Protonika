package com.valiukh.protonika.data.scripts

interface ScriptsRepository {

    fun addScript(name: String, script: String)

    fun getScript(name: String): String

    fun getAllScriptsNames(): List<String>

    fun getAllScripts(): Map<String, String>

    fun getActiveScriptName(): String

    fun setActiveScriptName(scriptName: String)

}