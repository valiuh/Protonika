package com.valiukh.protonika.minibasic


/**
 * Enum representing the different keywords in MiniBasic.
 */
enum class Keyword(val keyword: String) {
    REM("REM"),
    LET("LET"),
    PRINT("PRINT"),
    INPUT("INPUT"),
    IF("IF"),
    THEN("THEN"),
    ELSE("ELSE"),
    END("END"),
    FOR("FOR"),
    TO("TO"),
    STEP("STEP"),
    NEXT("NEXT"),
    GOTO("GOTO"),
    GOSUB("GOSUB"),
    SUB("SUB"),
    RETURN("RETURN"),
    STOP("STOP")
}

/**
 * Enum representing the different operators in MiniBasic.
 */
enum class Operator(val operator: String) {
    /**
     *Unary operator: get the absolute value of a number.
     */
    ABS("ABS"),

    /**
     *Unary operator: get the cosine of an angle.
     */
    COS("COS"),

    /**
     *Unary operator: get the sine of an angle.
     */
    SIN("SIN"),

    /**
     *Unary operator: get the tangent of an angle.
     */
    TAN("TAN"),

    /**
     *Unary operator: get the arccosine of a number.
     */
    ACOS("ACOS"),

    /**
     *Unary operator: get the arcsine of a number.
     */
    ASIN("ASIN"),

    /**
     *Unary operator: get the arctangent of a number.
     */
    ATN("ATN"),

    /**
     *Unary operator: get the logarithm of a number.
     */
    LOG("LOG"),

    /**
     *Unary operator: get the natural logarithm of a number.
     */
    LN("LN"),

    /**
     *Unary operator: get the exponential of a number.
     */
    EXP("EXP"),

    /**
     *Unary operator: get the square root of a number.
     */
    SQR("SQR"),

    /**
     *Binary operator: add two numbers.
     */
    PLUS("+"),

    /**
     *Binary operator: subtract two numbers.
     */
    MINUS("-"),

    /**
     *Binary operator: multiply two numbers.
     */
    MULTIPLY("*"),

    /**
     *Binary operator: divide two numbers.
     */
    DIVIDE("/"),

    /**
     *Binary operator: raise a number to a power.
     */
    GREATER_THAN(">"),

    /**
     *Binary operator: raise a number to a power.
     */
    LESS_THAN("<"),

    /**
     *Binary operator: raise a number to a power.
     */
    GREATER_THAN_OR_EQUAL(">="),

    /**
     *Binary operator: raise a number to a power.
     */
    LESS_THAN_OR_EQUAL("<="),

    /**
     *Binary operator: raise a number to a power.
     */
    EQUAL("="),

    /**
     *Binary operator: raise a number to a power.
     */
    POWER("^"),

    /**
     *Binary operator: raise a number to a power.
     */
    AND("AND"),

    /**
     *Binary operator: raise a number to a power.
     */
    OR("OR"),

    /**
     *Binary operator: raise a number to a power.
     */
    NOT("NOT"),

    /**
     *Binary operator: raise a number to a power.
     */
    XOR("XOR")
}

enum class Bracket(val bracket: String) {
    LEFT("("),
    RIGHT(")")
}