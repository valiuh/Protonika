package com.valiukh.protonika.virtualmachine.cli

import com.valiukh.protonika.virtualmachine.Mk61
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString

/**
 * Runs the MK61 CLI tool.
 *
 * Expects `-s <script_path>` to load the calculator script file.
 * The script file should use the `.mk61` extension, and its contents should
 * follow the script format described in `README.md`.
 *
 * Example script:
 * ```
 * П→X 0     ; Load N into X
 * X=0 12    ; If X == 0, jump to label 12
 * X→П 1     ; Store N in reg 1 (counter)
 * 1         ; Push 1
 * X→П 2     ; Store result in reg 2
 * LBL 05
 * П→X 2     ; Load result
 * П→X 1     ; Load counter
 * ×         ; Multiply
 * X→П 2     ; Store updated result
 * П→X 1     ; Load counter
 * 1         ; Push 1
 * -         ; Decrease counter
 * X→П 1     ; Update counter
 * X≠0 05    ; Loop while counter ≠ 0
 * П→X 2     ; Load final result into X
 * С/П       ; End
 * ```
 *
 * Supports optional `-r <y|yes>` to save execution output into a `.cnl` file.
 * The output file name is based on the script file name without its extension
 * and is saved in the current working directory.
 *
 * @param args command-line arguments for script input and optional result saving.
 */
fun main(args: Array<String>) {
    val arguments = args.toList()
    val scriptPathIndex = arguments.indexOf("-s")
    val outputIndex = arguments.indexOf("-r")

    if (scriptPathIndex == -1 || scriptPathIndex + 1 >= arguments.size) {
        println("Error: Missing required argument -s <script_path>")
        return
    }

    val scriptPath = arguments[scriptPathIndex + 1]
    val shouldSaveOutputResult = if (outputIndex != -1 && outputIndex + 1 < arguments.size) {
        arguments[outputIndex + 1]
    } else {
        null
    }

    try {
        val script = SystemFileSystem.source(Path(scriptPath)).buffered().use { it.readString() }
        val calculator = Mk61()
        calculator.uploadProgram(script)
        calculator.calculate()

        val X1 = calculator.printX1()

        val stackRegisters = calculator.printRegisters()
        val X = stackRegisters[0]
        val Y = stackRegisters[1]
        val Z = stackRegisters[2]
        val T = stackRegisters[3]

        val register0 = calculator.printDataRegister("0")
        val register1 = calculator.printDataRegister("1")
        val register2 = calculator.printDataRegister("2")
        val register3 = calculator.printDataRegister("3")
        val register4 = calculator.printDataRegister("4")
        val register5 = calculator.printDataRegister("5")
        val register6 = calculator.printDataRegister("6")
        val register7 = calculator.printDataRegister("7")
        val register8 = calculator.printDataRegister("8")
        val register9 = calculator.printDataRegister("9")
        val registerA = calculator.printDataRegister("a")
        val registerB = calculator.printDataRegister("b")
        val registerC = calculator.printDataRegister("c")
        val registerD = calculator.printDataRegister("d")
        val registerE = calculator.printDataRegister("e")

        val result =
            "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n"+
            "Stack:\n" +
            "X: ${X}\n" +
            "Y: ${Y}\n" +
            "Z: ${Z}\n" +
            "Z: ${T}\n" +
            "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n" +
            "Register of the Previous Result:\n" +
            "X1: ${X1}\n" +
            "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n" +
            "Registers:\n" +
            "0: ${register0} \t\t 7: ${register7}\n" +
            "1: ${register1} \t\t 8: ${register8}\n" +
            "2: ${register2} \t\t 9: ${register9}\n" +
            "3: ${register3} \t\t a: ${registerA}\n" +
            "\t\t b: ${registerB}\n" +
            "4: ${register4} \t\t c: ${registerC}\n" +
            "5: ${register5} \t\t d: ${registerD}\n" +
            "6: ${register6} \t\t e: ${registerE}\n" +
            "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n"

        println(result)

        if (shouldSaveOutputResult != null && (shouldSaveOutputResult == "y" || shouldSaveOutputResult == "yes")) {
            val filename = scriptPath.substringAfterLast("/")
            val extracted = filename.substringBefore(".")
            val outputPath = "${extracted}.cnl"
            SystemFileSystem.sink(Path(outputPath)).buffered().use { it.writeString(result) }
            println("Result was saved to $outputPath")
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}