package com.valiukh.protonika.minibasic

import com.valiukh.protonika.virtualmachine.Command

class CodeGenerator {
    private val variables = mutableMapOf<String, Char>()
    private val memoryRegisters = "0123456789ABCDE".toList()
    private var memoryIndex = 0

    fun generate(ast: List<ASTNode>): List<String> {
        val output = mutableListOf<String>()
        for (node in ast) {
            when (node) {
                is LetNode -> output.addAll(generateLet(node))
                is IfNode -> output.addAll(generateIf(node))
                is GotoNode -> output.add("БП ${node.line}")
                is EndNode -> output.add(Command.STOP.commandMnemonics)
                is PrintNode -> output.add("П→X ${getOrAssignRegister(node.expression)}")
                is InputNode -> output.add("X→П ${getOrAssignRegister(node.variable)}")
                is GoSubNode -> output.add("ПП ${node.name}")
                is SubroutineNode -> {
                    output.add("${node.name}:")
                    node.body.forEach { output.addAll(generate(listOf(it))) }
                    output.add(Command.RTN.commandMnemonics)
                }
                is ForLoopNode -> {
                    output.add("X→П ${getOrAssignRegister(node.variable)}")
                    node.body.forEach { output.addAll(generate(listOf(it))) }
                    output.add("X→П ${getOrAssignRegister(node.variable)}")
                }
                else -> throw IllegalArgumentException("Unknown node type: $node")
            }
        }
        return output
    }

    private fun generateLet(node: LetNode): List<String> {
        return listOf("${node.value}", "X→П ${getOrAssignRegister(node.variable)}")
    }

    private fun generateIf(node: IfNode): List<String> {
        return emptyList()
//        return listOf("П→X ${getOrAssignRegister(node.condition)}", "X≥0", node.targetLine.toString())
    }

    private fun getOrAssignRegister(varName: String): Char {
        return variables.getOrPut(varName) {
            if (memoryIndex >= memoryRegisters.size) throw IllegalArgumentException("Too many variables")
            memoryRegisters[memoryIndex++]
        }
    }
}