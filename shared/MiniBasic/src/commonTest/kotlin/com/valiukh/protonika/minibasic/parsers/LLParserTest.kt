package com.valiukh.protonika.minibasic.parsers

import com.valiukh.protonika.minibasic.ForLoopNode
import com.valiukh.protonika.minibasic.GoSubNode
import com.valiukh.protonika.minibasic.GotoNode
import com.valiukh.protonika.minibasic.IdentifierNode
import com.valiukh.protonika.minibasic.IfNode
import com.valiukh.protonika.minibasic.InputNode
import com.valiukh.protonika.minibasic.LetNode
import com.valiukh.protonika.minibasic.OperatorBinaryNode
import com.valiukh.protonika.minibasic.OperatorUnaryNode
import com.valiukh.protonika.minibasic.PrintNode
import com.valiukh.protonika.minibasic.Token
import com.valiukh.protonika.minibasic.TokenType

import kotlin.test.Test
import kotlin.test.assertNull

class LLParserTest {
    private fun assert(condition: Boolean) = kotlin.test.assertTrue(condition)
    private fun parse(tokens: List<Token>) = LLParser().apply { updateTokens(tokens) }.parse()

    @Test
    fun `Scenario - LET`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.KEYWORD, "LET"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.OPERATOR, "="),
            Token(TokenType.NUMBER, "5"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is LetNode)

        val letNode = result as LetNode
        assert(letNode.variable == "x")
        assert(letNode.value == "5")
    }

    @Test
    fun `Scenario - LET with unar expression`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.KEYWORD, "LET"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.OPERATOR, "="),
            Token(TokenType.KEYWORD, "SIN"),
            Token(TokenType.OPERATOR, "("),
            Token(TokenType.NUMBER, "30"),
            Token(TokenType.OPERATOR, ")"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is LetNode)

        val letNode = result as LetNode
        assert(letNode.variable == "x")
        assertNull(letNode.value)

        val expressions = letNode.expression
        assert(expressions.size == 1)

        val expression = expressions[0]
        assert(expression is OperatorUnaryNode)

        val operatorUnaryNode = expression as OperatorUnaryNode
        assert(operatorUnaryNode.operator == "SIN")
        assert(operatorUnaryNode.value == "30")
    }

    @Test
    fun `Scenario - LET with binary expression`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.KEYWORD, "LET"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.OPERATOR, "="),
            Token(TokenType.NUMBER, "5"),
            Token(TokenType.OPERATOR, "+"),
            Token(TokenType.NUMBER, "5"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is LetNode)

        val letNode = result as LetNode
        assert(letNode.variable == "x")
        assertNull(letNode.value)

        val expressions = letNode.expression
        assert(expressions.size == 1)
        val expression = expressions[0]
        assert(expression is OperatorBinaryNode)

        val operatorBinaryNode = expression as OperatorBinaryNode
        assert(operatorBinaryNode.operator == "+")
        assert(operatorBinaryNode.leftValue == "5")
        assert(operatorBinaryNode.rightValue == "5")
    }

    @Test
    fun `Scenario - x = 5`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.OPERATOR, "="),
            Token(TokenType.NUMBER, "5"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is IdentifierNode)

        val letNode = result as IdentifierNode
        assert(letNode.variable == "x")
        assert(letNode.value == "5")
    }

    @Test
    fun `Scenario - x = SIN(30)`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.OPERATOR, "="),
            Token(TokenType.KEYWORD, "SIN"),
            Token(TokenType.OPERATOR, "("),
            Token(TokenType.NUMBER, "30"),
            Token(TokenType.OPERATOR, ")"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is IdentifierNode)

        val node = result as IdentifierNode
        assert(node.variable == "x")
        assertNull(node.value)

        val expressions = node.expression
        assert(expressions.size == 1)

        val expression = expressions[0]
        assert(expression is OperatorUnaryNode)

        val operatorUnaryNode = expression as OperatorUnaryNode
        assert(operatorUnaryNode.operator == "SIN")
        assert(operatorUnaryNode.value == "30")
    }

    @Test
    fun `Scenario - x = 5 + 5`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.OPERATOR, "="),
            Token(TokenType.NUMBER, "5"),
            Token(TokenType.OPERATOR, "+"),
            Token(TokenType.NUMBER, "5"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is IdentifierNode)

        val node = result as IdentifierNode
        assert(node.variable == "x")
        assertNull(node.value)

        val expressions = node.expression
        assert(expressions.size == 1)
        val expression = expressions[0]
        assert(expression is OperatorBinaryNode)

        val operatorBinaryNode = expression as OperatorBinaryNode
        assert(operatorBinaryNode.operator == "+")
        assert(operatorBinaryNode.leftValue == "5")
        assert(operatorBinaryNode.rightValue == "5")
    }

    @Test
    fun `Scenario - PRINT`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "20"),
            Token(TokenType.KEYWORD, "PRINT"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is PrintNode)

        val printNode = result as PrintNode
        assert(printNode.expression == "x")
    }

    @Test
    fun `Scenario - INPUT`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "30"),
            Token(TokenType.KEYWORD, "INPUT"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is InputNode)

        val inputNode = result as InputNode
        assert(inputNode.variable == "x")
    }

    @Test
    fun `Scenario - GOTO`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "40"),
            Token(TokenType.KEYWORD, "GOTO"),
            Token(TokenType.NUMBER, "50"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is GotoNode)

        val gotoNode = result as GotoNode
        assert(gotoNode.line == 50)
    }

    @Test
    fun `Scenario - GOSUB`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "40"),
            Token(TokenType.KEYWORD, "GOSUB"),
            Token(TokenType.NUMBER, "function"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is GoSubNode)

        val gotoSubNode = result as GoSubNode
        assert(gotoSubNode.name == "function")
    }

    @Test
    fun `Scenario - IF`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "40"),
            Token(TokenType.KEYWORD, "IF"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.OPERATOR, ">"),
            Token(TokenType.NUMBER, "5"),
            Token(TokenType.KEYWORD, "THEN"),
            Token(TokenType.KEYWORD, "GOTO"),
            Token(TokenType.NUMBER, "50"),
            Token(TokenType.KEYWORD, "END"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 3)
        val result = abstractSyntaxTree[1]
        assert(result is IfNode)

        val ifNode = result as IfNode
        assert(ifNode.expression.size == 1)

        val expression = ifNode.expression[0]
        assert(expression is OperatorBinaryNode)

        val operatorBinaryNode = expression as OperatorBinaryNode
        assert(operatorBinaryNode.leftValue == "x")
        assert(operatorBinaryNode.operator == ">")
        assert(operatorBinaryNode.rightValue == "5")

        assert(ifNode.body.size == 1)

        val bodyNode = ifNode.body[0]
        assert(bodyNode is GotoNode)

        val gotoNode = bodyNode as GotoNode
        assert(gotoNode.line == 50)
    }

    @Test
    fun `Scenario - IF ELSE`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "40"),
            Token(TokenType.KEYWORD, "IF"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.OPERATOR, ">"),
            Token(TokenType.NUMBER, "5"),
            Token(TokenType.KEYWORD, "THEN"),
            Token(TokenType.KEYWORD, "GOTO"),
            Token(TokenType.NUMBER, "50"),
            Token(TokenType.KEYWORD, "ELSE"),
            Token(TokenType.KEYWORD, "GOTO"),
            Token(TokenType.NUMBER, "60"),
            Token(TokenType.KEYWORD, "END"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 3)
        val result = abstractSyntaxTree[1]
        assert(result is IfNode)

        val ifNode = result as IfNode
        assert(ifNode.expression.size == 1)

        val expression = ifNode.expression[0]
        assert(expression is OperatorBinaryNode)

        val operatorBinaryNode = expression as OperatorBinaryNode
        assert(operatorBinaryNode.leftValue == "x")
        assert(operatorBinaryNode.operator == ">")
        assert(operatorBinaryNode.rightValue == "5")

        val bodyNode = ifNode.body[0]
        assert(bodyNode is GotoNode)

        val ifGotoNode = bodyNode as GotoNode
        assert(ifGotoNode.line == 50)

        val elseNode = ifNode.bodyElse[0]
        assert(elseNode is GotoNode)

        val elseGotoNode = elseNode as GotoNode
        assert(elseGotoNode.line == 60)
    }

    @Test
    fun `Scenario - FOR empty body`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.KEYWORD, "FOR"),
            Token(TokenType.IDENTIFIER, "i"),
            Token(TokenType.OPERATOR, "="),
            Token(TokenType.NUMBER, "1"),
            Token(TokenType.KEYWORD, "TO"),
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.KEYWORD, "STEP"),
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.KEYWORD, "NEXT"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is ForLoopNode)

        val forNode = result as ForLoopNode
        assert(forNode.variable == "i")
        assert(forNode.start == 1)
        assert(forNode.end == 10)
        assert(forNode.step == 10)
        assert(forNode.body.isEmpty())
    }

    @Test
    fun `Scenario - FOR with body`() {
        val tokens = listOf(
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.KEYWORD, "FOR"),
            Token(TokenType.IDENTIFIER, "i"),
            Token(TokenType.OPERATOR, "="),
            Token(TokenType.NUMBER, "1"),
            Token(TokenType.KEYWORD, "TO"),
            Token(TokenType.NUMBER, "10"),
            Token(TokenType.KEYWORD, "STEP"),
            Token(TokenType.NUMBER, "2"),
            Token(TokenType.NUMBER, "20"),
            Token(TokenType.KEYWORD, "LET"),
            Token(TokenType.IDENTIFIER, "x"),
            Token(TokenType.OPERATOR, "="),
            Token(TokenType.NUMBER, "20"),
            Token(TokenType.KEYWORD, "NEXT"),
            Token(TokenType.EOF, "EOF")
        )

        val abstractSyntaxTree = parse(tokens)
        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is ForLoopNode)

        val forNode = result as ForLoopNode
        assert(forNode.variable == "i")
        assert(forNode.start == 1)
        assert(forNode.end == 10)
        assert(forNode.step == 2)
        assert(forNode.body.size == 1)

        val bodyNode = forNode.body[0]
        assert(bodyNode is LetNode)

        val letNode = bodyNode as LetNode
        assert(letNode.variable == "x")
        assert(letNode.value == "20")
    }

}