package com.valiukh.protonika.minibasic.lexer_and_parser

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
import com.valiukh.protonika.minibasic.parseLL
import com.valiukh.protonika.minibasic.parsers.LLParser
import com.valiukh.protonika.minibasic.tokenize
import kotlin.test.Test
import kotlin.test.assertNull

class LexerLLParserTest {
    private fun assert(condition: Boolean) = kotlin.test.assertTrue(condition)
    private val parser = LLParser()

    @Test
    fun `Scenario - LET`() {
        val script = "10 LET x = 5"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]

        val letNode = result as LetNode
        assert(letNode.variable == "x")
        assert(letNode.value == "5")
    }

    @Test
    fun `Scenario - LET with unar expression`() {
        val script = "10 LET x = SIN(30)"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

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
        val script = "10 LET x = 5 + 5"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

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
        val script = "10 x = 5"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]

        val node = result as IdentifierNode
        assert(node.variable == "x")
        assert(node.value == "5")
    }

    @Test
    fun `Scenario - x = 5 + 5`() {
        val script = "10 x = 5 + 5"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

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
    fun `Scenario - x = SIN(30)`() {
        val script = "10 x = SIN(30)"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

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
    fun `Scenario - PRINT`() {
        val script = "20 PRINT x"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]

        val printNode = result as PrintNode
        assert(printNode.expression == "x")
    }

    @Test
    fun `Scenario - INPUT`() {
        val script = "30 INPUT x"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]

        val inputNode = result as InputNode
        assert(inputNode.variable == "x")
    }

    @Test
    fun `Scenario - GOTO`() {
        val script = "40 GOTO 50"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is GotoNode)

        val gotoNode = result as GotoNode
        assert(gotoNode.line == 50)
    }

    @Test
    fun `Scenario - GOSUB`() {
        val script = "40 GOSUB function"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is GoSubNode)

        val gotoSubNode = result as GoSubNode
        assert(gotoSubNode.name == "function")
    }

    @Test
    fun `Scenario - IF`() {
        val script = "40 IF x > 5 THEN GOTO 50 END"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

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
        val script = "40 IF x > 5 THEN GOTO 50 ELSE GOTO 60 END"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 3)
        val result = abstractSyntaxTree[1]
        assert(result is IfNode)

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

        val ifGotoNode = bodyNode as GotoNode
        assert(ifGotoNode.line == 50)

        val elseNode = ifNode.bodyElse[0]
        assert(elseNode is GotoNode)

        val elseGotoNode = elseNode as GotoNode
        assert(elseGotoNode.line == 60)
    }

    @Test
    fun `Scenario - IF with multiple expressions`() {
        val script = "40 IF x > 5 AND y < 10 THEN GOTO 50 END"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 3)
        val result = abstractSyntaxTree[1]
        assert(result is IfNode)

        val ifNode = result as IfNode
        assert(ifNode.body.size == 1)

        val bodyNode = ifNode.body[0]
        assert(bodyNode is GotoNode)

        val gotoNode = bodyNode as GotoNode
        assert(gotoNode.line == 50)

        val expressions = ifNode.expression
        assert(expressions.size == 1)

        val expression = expressions[0]
        assert(expression is OperatorBinaryNode)

        val operatorBinaryNode = expression as OperatorBinaryNode
        assert(operatorBinaryNode.operator == "AND")
        assertNull(operatorBinaryNode.leftValue)
        assertNull(operatorBinaryNode.rightValue)

        val leftExpression = operatorBinaryNode.leftExpression
        assert(leftExpression.size == 1)
        val leftExpressionNode = leftExpression[0]
        assert(leftExpressionNode is OperatorBinaryNode)

        val leftOperatorBinaryNode = leftExpressionNode as OperatorBinaryNode
        assert(leftOperatorBinaryNode.operator == ">")
        assert(leftOperatorBinaryNode.leftValue == "x")
        assert(leftOperatorBinaryNode.rightValue == "5")

        val rightExpression = operatorBinaryNode.rightExpression
        assert(rightExpression.size == 1)
        val rightExpressionNode = rightExpression[0]
        assert(rightExpressionNode is OperatorBinaryNode)

        val rightOperatorBinaryNode = rightExpressionNode as OperatorBinaryNode
        assert(rightOperatorBinaryNode.operator == "<")
        assert(rightOperatorBinaryNode.leftValue == "y")
        assert(rightOperatorBinaryNode.rightValue == "10")
    }

    @Test
    fun `Scenario - FOR empty body`() {
        val script = "50 FOR i = 1 TO 10 STEP 2 NEXT"
        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is ForLoopNode)

        val forNode = result as ForLoopNode
        assert(forNode.variable == "i")
        assert(forNode.start == 1)
        assert(forNode.end == 10)
        assert(forNode.step == 2)
        assert(forNode.body.isEmpty())
    }

    @Test
    fun `Scenario - FOR with body`() {
        val script = """
            50 FOR i = 1 TO 10 STEP 2
            60 PRINT i
            70 NEXT
        """.trimIndent()

        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

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
        assert(bodyNode is PrintNode)

        val printNode = bodyNode as PrintNode
        assert(printNode.expression == "i")
    }

    @Test
    fun `Scenario - FOR unar expression body`() {
        val script = """
            50 FOR i = 1 TO 10 STEP 2
            60 LET x = SIN(i)
            70 NEXT
        """.trimIndent()

        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

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
        assertNull(letNode.value)

        val expressions = letNode.expression
        assert(expressions.size == 1)

        val expression = expressions[0]
        assert(expression is OperatorUnaryNode)

        val operatorUnaryNode = expression as OperatorUnaryNode
        assert(operatorUnaryNode.operator == "SIN")
        assert(operatorUnaryNode.value == "i")
    }

    @Test
    fun `Scenario - FOR binary expression body`() {
        val script = """
            50 FOR i = 1 TO 10 STEP 2
            60 LET x = i + 5
            70 NEXT
        """.trimIndent()

        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

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
        assertNull(letNode.value)

        val expressions = letNode.expression
        assert(expressions.size == 1)

        val expression = expressions[0]
        assert(expression is OperatorBinaryNode)

        val operatorBinaryNode = expression as OperatorBinaryNode
        assert(operatorBinaryNode.leftValue == "i")
        assert(operatorBinaryNode.operator == "+")
        assert(operatorBinaryNode.rightValue == "5")
    }

    @Test
    fun `Scenario - FOR with multiple body`() {
        val script = """
            50 FOR i = 1 TO 10 STEP 2
            60 LET x = i + 5
            70 PRINT x
            80 NEXT
        """.trimIndent()

        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 2)

        val result = abstractSyntaxTree[1]
        assert(result is ForLoopNode)

        val forNode = result as ForLoopNode
        assert(forNode.variable == "i")
        assert(forNode.start == 1)
        assert(forNode.end == 10)
        assert(forNode.step == 2)
        assert(forNode.body.size == 2)

        val bodyNode1 = forNode.body[0]
        assert(bodyNode1 is LetNode)

        val letNode = bodyNode1 as LetNode
        assert(letNode.variable == "x")
        assertNull(letNode.value)

        val expressions = letNode.expression
        assert(expressions.size == 1)

        val expression = expressions[0]
        assert(expression is OperatorBinaryNode)

        val operatorBinaryNode = expression as OperatorBinaryNode
        assert(operatorBinaryNode.leftValue == "i")
        assert(operatorBinaryNode.operator == "+")
        assert(operatorBinaryNode.rightValue == "5")

        val bodyNode2 = forNode.body[1]
        assert(bodyNode2 is PrintNode)

        val printNode = bodyNode2 as PrintNode
        assert(printNode.expression == "x")
    }

    @Test
    fun `Scenario - FOR with nested FOR`() {
        val script = """
            50 FOR i = 1 TO 10 STEP 2
            60 FOR j = 1 TO 10 STEP 2
            70 PRINT j
            80 NEXT
            90 NEXT
        """.trimIndent()

        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

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
        assert(bodyNode is ForLoopNode)

        val nestedForNode = bodyNode as ForLoopNode
        assert(nestedForNode.variable == "j")
        assert(nestedForNode.start == 1)
        assert(nestedForNode.end == 10)
        assert(nestedForNode.step == 2)
        assert(nestedForNode.body.size == 1)

        val nestedBodyNode = nestedForNode.body[0]
        assert(nestedBodyNode is PrintNode)

        val printNode = nestedBodyNode as PrintNode
        assert(printNode.expression == "j")
    }

    @Test
    fun `Scenario - simple code`() {
        val script = """
            10 LET first = 10
            20 LET second = 30
            30 LET result = first + second
            40 IF result > 0 THEN GOTO 60 END
            50 LET test = 50
        """.trimIndent()

        val abstractSyntaxTree = script
            .tokenize()
            .parseLL(parser)

        assert(abstractSyntaxTree.size == 11)
        val letNode1 = abstractSyntaxTree[1] as LetNode
        assert(letNode1.variable == "first")
        assert(letNode1.value == "10")

        val letNode2 = abstractSyntaxTree[3] as LetNode
        assert(letNode2.variable == "second")
        assert(letNode2.value == "30")

        val letNode3 = abstractSyntaxTree[5] as LetNode
        assert(letNode3.variable == "result")
        assert(letNode3.expression.size == 1)

        val operatorBinaryNode = letNode3.expression[0] as OperatorBinaryNode
        assert(operatorBinaryNode.operator == "+")
        assert(operatorBinaryNode.leftValue == "first")
        assert(operatorBinaryNode.rightValue == "second")

        val ifNode = abstractSyntaxTree[7] as IfNode
        assert(ifNode.expression.size == 1)

        val operatorBinaryNode2 = ifNode.expression[0] as OperatorBinaryNode
        assert(operatorBinaryNode2.operator == ">")
        assert(operatorBinaryNode2.leftValue == "result")
        assert(operatorBinaryNode2.rightValue == "0")

        assert(ifNode.body.size == 1)
        val gotoNode = ifNode.body[0] as GotoNode
        assert(gotoNode.line == 60)

        val letNode4 = abstractSyntaxTree[10] as LetNode
        assert(letNode4.variable == "test")
        assert(letNode4.value == "50")
    }

}