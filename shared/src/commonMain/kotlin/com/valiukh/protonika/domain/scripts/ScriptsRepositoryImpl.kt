package com.valiukh.protonika.domain.scripts

import com.valiukh.protonika.data.scripts.ScriptsRepository

class ScriptsRepositoryImpl : ScriptsRepository {

    val scripts: MutableMap<String, String> = mutableMapOf()

    private var currentActiveScriptName: String = ""

    override fun addScript(name: String, script: String) {
        scripts[name] = script
    }

    override fun getScript(name: String): String {
        return scripts[name] ?: throw IllegalArgumentException("Script not found")
    }

    override fun getAllScriptsNames(): List<String> {
        return scripts.keys.toList()
    }

    override fun getAllScripts(): Map<String, String> {
        return scripts.toMap()
    }

    override fun getActiveScriptName(): String {
        return currentActiveScriptName
    }

    override fun setActiveScriptName(scriptName: String) {
        if(scripts.isNotEmpty()){
            if (!scripts.containsKey(scriptName)) {
                throw IllegalArgumentException("Script not found")
            }
            currentActiveScriptName = scriptName
        }
    }
}