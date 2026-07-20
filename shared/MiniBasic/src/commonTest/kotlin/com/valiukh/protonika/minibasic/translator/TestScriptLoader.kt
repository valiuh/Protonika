package com.valiukh.protonika.minibasic.translator

/**
 * Loads a MiniBasic test script from the shared `test_scripts` test resources.
 *
 * @param scriptName The script file name, e.g. `"factorial.mb61"`.
 * @return The raw UTF-8 contents of the script.
 */
internal expect fun loadTestScript(scriptName: String): String
