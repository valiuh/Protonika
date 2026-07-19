package com.valiukh.protonika.minibasic

/**
 * Enum representing the different types of tokens.
 */
enum class TokenType {
    NUMBER,
    IDENTIFIER,
    KEYWORD,
    OPERATOR,
    BRACKET,
    NEWLINE,
    EOF
}

/**
 * Data class representing a token with a type and value.
 *
 * @property type The type of the token.
 * @property value The value of the token.
 */
data class Token(val type: TokenType, val value: String)

/**
 * A lexer for tokenizing MiniBasic source code.
 */
class Lexer {

    private var position = 0

    /**
     * Tokenizes the input string into a list of tokens.
     *
     * @param input The input string to tokenize.
     * @return A list of tokens.
     */
    fun tokenize(input: String): List<Token> {
        val tokens = mutableListOf<Token>()
        while (position < input.length) {
            val char = input[position]
            when {
                char.isNewLine() -> {
                    tokens.add(Token(TokenType.NEWLINE, "\\n"))
                    position++
                }
                char.isWhitespace() -> position++
                char.isDigit() -> tokens.add(readNumber(input))
                char.isLetter() -> tokens.add(readIdentifierOrKeyword(input))
                char.isOperator() -> tokens.add(readOperator(input))
                char.isBracket() -> tokens.add(readBracket(input))
                else -> throw IllegalArgumentException("Unknown character: $char")
            }
        }
        tokens.add(Token(TokenType.EOF, "EOF"))
        return tokens
    }

    /**
     * Reads a number token from the input string.
     *
     * @param input The input string.
     * @return A number token.
     */
    private fun readNumber(input: String): Token {
        val start = position
        while (position < input.length && input[position].isDigit()) position++
        return Token(TokenType.NUMBER, input.substring(start, position))
    }

    /**
     * Reads an identifier or keyword token from the input string.
     *
     * @param input The input string.
     * @return An identifier or keyword token.
     */
    private fun readIdentifierOrKeyword(input: String): Token {
        val start = position
        while (position < input.length && input[position].isLetterOrDigit()) position++
        val value = input.substring(start, position)
        return if (isKeyword(value)) Token(TokenType.KEYWORD, value) else Token(TokenType.IDENTIFIER, value)
    }

    /**
     * Reads an operator token from the input string.
     *
     * @param input The input string.
     * @return An operator token.
     */
    private fun readOperator(input: String): Token {
        val operator = input[position++].toString()
        return Token(TokenType.OPERATOR, operator)
    }

    /**
     * Reads a bracket token from the input string.
     *
     * @param input The input string.
     * @return A bracket token.
     */
    private fun readBracket(input: String): Token {
        val bracket = input[position++].toString()
        return Token(TokenType.BRACKET, bracket)
    }

    /**
     * Checks if the character is an operator.
     *
     * @return True if the character is an operator, false otherwise.
     */
    private fun Char.isOperator() =
        Operator.entries.any { it.operator == this.toString() }

    /**
     * Checks if the character is a newline.
     *
     * @return True if the character is a newline, false otherwise.
     */
    private fun Char.isNewLine() = this == '\n'

    /**
     * Checks if the character is a whitespace.
     *
     * @return True if the character is a whitespace, false otherwise.
     */
    private fun Char.isBracket() = Bracket.entries.any { it.bracket == this.toString() }

    /**
     * Checks if the given term is a keyword.
     *
     * @param term The term to check.
     * @return True if the term is a keyword, false otherwise.
     */
    private fun isKeyword(term: String) =
        Keyword.entries.any { it.keyword == term }

}