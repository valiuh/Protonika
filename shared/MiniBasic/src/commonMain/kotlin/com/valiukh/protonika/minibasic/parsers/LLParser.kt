package com.valiukh.protonika.minibasic.parsers


import com.valiukh.protonika.minibasic.*
import com.valiukh.protonika.minibasic.ASTNode
import com.valiukh.protonika.minibasic.EmptyNode
import com.valiukh.protonika.minibasic.EndNode
import com.valiukh.protonika.minibasic.ForLoopNode
import com.valiukh.protonika.minibasic.GoSubNode
import com.valiukh.protonika.minibasic.GotoNode
import com.valiukh.protonika.minibasic.IdentifierNode
import com.valiukh.protonika.minibasic.IfNode
import com.valiukh.protonika.minibasic.InputNode
import com.valiukh.protonika.minibasic.LetNode
import com.valiukh.protonika.minibasic.LineNumberNode
import com.valiukh.protonika.minibasic.OperatorBinaryNode
import com.valiukh.protonika.minibasic.OperatorUnaryNode
import com.valiukh.protonika.minibasic.Parser
import com.valiukh.protonika.minibasic.PrintNode
import com.valiukh.protonika.minibasic.StopNode
import com.valiukh.protonika.minibasic.SubroutineNode
import com.valiukh.protonika.minibasic.UnknownNode

class LLParser : Parser {

    private var tokens: List<Token> = emptyList()
    private var position = 0

    override fun updateTokens(tokens: List<Token>) {
        this.tokens = tokens
        this.position = 0
    }

    override fun parse(): List<ASTNode> {
        val nodes = mutableListOf<ASTNode>()
        while (!isAtEnd()) {
            parseStatement()?.let { node ->
                if (node !is EmptyNode){
                    nodes.add(node)
                } else {
                    position++ // Skip newline
                }
            }
        }
        return nodes
    }

    private fun parseStatement(): ASTNode? {
        val token = peek() ?: return null
        return when (token.type) {
            TokenType.NUMBER -> parseLineNumber()
            TokenType.IDENTIFIER -> parseIdentifierNode()
            TokenType.KEYWORD -> parseKeyword(token)
            TokenType.NEWLINE -> EmptyNode()
            else -> UnknownNode(token.value)
        }
    }

    private fun parseLineNumber(): ASTNode {
        val token = advance()
        return LineNumberNode(lineNumber = token.value)
    }

    private fun parseIdentifierNode(isLet: Boolean = false): ASTNode {
        val variable = advance().value
        consumeOperator()

        val current = peek()
        val next = peek(1)

        return when {
            (current?.type == TokenType.NUMBER || current?.type == TokenType.IDENTIFIER) && next?.type == TokenType.OPERATOR -> {
                val expression = listOf(parseKeyword(next))
                createAssignmentNode(
                    variable = variable,
                    expression = expression,
                    isLet = isLet
                )
            }
            current?.type == TokenType.NUMBER -> {
                val value = advance().value
                createAssignmentNode(
                    variable = variable,
                    value = value,
                    isLet = isLet
                )
            }
            else -> {
                val expression = listOf(parseKeyword(current!!))
                createAssignmentNode(
                    variable = variable,
                    expression = expression,
                    isLet = isLet
                )
            }
        }
    }

    private fun parseKeyword(token: Token): ASTNode {
        return when (token.value) {
            Keyword.LET.keyword -> parseLet()
            Keyword.PRINT.keyword -> parsePrint()
            Keyword.INPUT.keyword -> parseInput()
            Keyword.IF.keyword -> parseIf()
            Keyword.FOR.keyword -> parseFor()
            Keyword.GOTO.keyword -> parseGoto()
            Keyword.GOSUB.keyword -> parseGosub()
            Keyword.SUB.keyword -> parseSubroutine()
            Keyword.END.keyword -> advance().let { EndNode(it.value) }
            Keyword.STOP.keyword -> StopNode()
            in Operator.entries.map { it.name } -> parseUnaryOperator()
            in Operator.entries.map { it.operator } -> parseBinaryOperator()
            else -> UnknownNode(token.value)
        }
    }

    private fun parseLet(): ASTNode {
        advance() // Skip LET
        peek()

        return parseIdentifierNode(
            isLet = true
        )

    }

    private fun parsePrint(): ASTNode {
        advance() // Skip PRINT

        return PrintNode(
            expression = advance().value
        )
    }

    private fun parseInput(): ASTNode {
        advance() // Skip INPUT

        return InputNode(
            variable = advance().value
        )
    }

    private fun parseIf(): ASTNode {
        advance() // Skip IF

        val leftVar = advanceOrThrow("Expected variable after IF").value
        val leftOp = advanceOrThrow("Expected operator after variable").value
        val leftVal = advanceOrThrow("Expected value after operator").value

        var expression: List<ASTNode> = emptyList()

        val next = peek()
        if (next?.value == "AND" || next?.value == "OR") {
            val logicalOp = advance().value

            val rightVar = advanceOrThrow("Expected second condition variable").value
            val rightOp = advanceOrThrow("Expected operator").value
            val rightVal = advanceOrThrow("Expected value").value

            val leftExpr = OperatorBinaryNode(
                operator = leftOp,
                leftValue = leftVar,
                rightValue = leftVal
            )

            val rightExpr = OperatorBinaryNode(
                operator = rightOp,
                leftValue = rightVar,
                rightValue = rightVal
            )

            expression = listOf(
                OperatorBinaryNode(
                    operator = logicalOp,
                    leftExpression = listOf(leftExpr),
                    rightExpression = listOf(rightExpr)
                )
            )
        }

        consumeValue(Keyword.THEN.keyword)

        val body = mutableListOf<ASTNode>()
        val elseBody = mutableListOf<ASTNode>()

        while (!matchValue(Keyword.END.keyword, Keyword.ELSE.keyword)) {
            val node = parseStatement() ?: break
            body.add(node)
        }

        if (matchValue(Keyword.ELSE.keyword)) {
            position++ // Skip ELSE
            while (!matchValue(Keyword.END.keyword)) {
                val node = parseStatement() ?: break
                elseBody.add(node)
            }
        }

        expression = expression.ifEmpty {
            listOf(
                OperatorBinaryNode(
                    operator = leftOp,
                    leftValue = leftVar,
                    rightValue = leftVal
                )
            )
        }

        return IfNode(
            expression = expression,
            body = body,
            bodyElse = elseBody
        )
    }

    private fun parseFor(): ASTNode {
        advance() // Skip FOR
        val variable = advanceOrThrow("Expected loop variable").value
        consumeValue("=")
        val start = advanceOrThrow("Expected start value").value.toInt()
        consumeValue(Keyword.TO.keyword)
        val end = advanceOrThrow("Expected end value").value.toInt()

        val step = if (matchValue(Keyword.STEP.keyword)) {
            position++ // Skip STEP
            advanceOrThrow("Expected step value").value.toInt()
        } else 1

        val body = mutableListOf<ASTNode>()

        // 🚨 SAFELY PARSE LOOP BODY
        while (!isAtEnd()) {
            val token = peek() ?: break

            if (token.value == Keyword.NEXT.keyword) {
                advance() // consume NEXT
                break
            }

            if (token.type == TokenType.NEWLINE) {
                position++ // Skip newline
                continue
            }

            val node = parseStatement()
            if (node != null) {
                body.add(node)
            } else {
                position++ // safeguard
            }
        }

        return ForLoopNode(
            variable = variable,
            start = start,
            end = end,
            step = step,
            body = body.filter { it !is LineNumberNode }
        )
    }


    private fun parseGoto(): ASTNode {
        advance() // Skip GOTO

        return GotoNode(
            line = advance().value.toInt()
        )
    }

    private fun parseGosub(): ASTNode {
        advance() // Skip GOSUB

        return GoSubNode(
            name = advance().value
        )
    }

    private fun parseSubroutine(): ASTNode {
        advance() // Skip SUB
        val name = advance().value
        val body = mutableListOf<ASTNode>()
        while (!matchValue(Keyword.RETURN.keyword)) {
            parseStatement()?.let { body.add(it) }
        }
        advance() // Skip RETURN

        return SubroutineNode(
            name = name,
            body = body
        )
    }

    private fun parseUnaryOperator(): ASTNode {
        val operator = advance().value
        consumeValue("(")
        val value = advance().value
        consumeValue(")")

        return OperatorUnaryNode(
            operator = operator,
            value = value
        )
    }

    private fun parseBinaryOperator(): ASTNode {
        val left = advance().value
        val operator = advance().value
        val right = advance().value

        return OperatorBinaryNode(
            operator = operator,
            leftValue = left,
            rightValue = right
        )
    }

    private fun createAssignmentNode(
        variable: String,
        expression: List<ASTNode> = emptyList(),
        value: String? = null,
        isLet: Boolean = false
    ): ASTNode {

        return if (isLet)
            LetNode(
                variable = variable,
                value = value,
                expression = expression
            )
        else
            IdentifierNode(
                variable = variable,
                value = value,
                expression = expression
            )
    }

    private fun peek(offset: Int = 0): Token? =
        tokens.getOrNull(position + offset)

    private fun advance(): Token =
        tokens[position++]

    private fun advanceOrThrow(message: String): Token =
        if (!isAtEnd())
            advance()
        else
            throw IllegalStateException(message)

    private fun consumeOperator(): Token {
        val token = advance()
        require(token.type == TokenType.OPERATOR) { "Expected '=' after identifier" }
        return token
    }

    private fun consumeValue(expected: String): Token {
        val token = advance()
        require(token.value == expected) { "Expected '$expected', but got '${token.value}'" }
        return token
    }

    private fun matchValue(vararg expected: String): Boolean {
        return expected.any { peek()?.value == it }
    }

    private fun isAtEnd(): Boolean = peek()?.type == TokenType.EOF
}