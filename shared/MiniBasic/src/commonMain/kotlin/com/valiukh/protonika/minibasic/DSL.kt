package com.valiukh.protonika.minibasic

import com.valiukh.protonika.minibasic.parsers.LLParser

fun String.tokenize(): List<Token> = Lexer().tokenize(this)

fun List<Token>.parseLL(parser: Parser): List<ASTNode> {
    parser.updateTokens(this)
    return parser.parse()
}

fun List<ASTNode>.generate(): List<String> = CodeGenerator().generate(this)