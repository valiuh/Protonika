package com.valiukh.protonika.compose.components.input

/**
 * Button types for the main MK-61 layout.
 *
 * UI component implementation will be added later.
 */
enum class MkButtonType(val text: String) {
    BUTTON_0("0"),
    BUTTON_1("1"),
    BUTTON_2("2"),
    BUTTON_3("3"),
    BUTTON_4("4"),
    BUTTON_5("5"),
    BUTTON_6("6"),
    BUTTON_7("7"),
    BUTTON_8("8"),
    BUTTON_9("9"),

    BUTTON_DECIMAL_SEPARATOR("."),

    BUTTON_MINUS("-"),
    BUTTON_DIVIDE("÷"),
    BUTTON_PLUS("+"),
    BUTTON_MULTIPLY("×"),

    BUTTON_F("F"),
    BUTTON_K("K"),

    BUTTON_STEP_FORWARD("→ШГ"),
    BUTTON_STEP_BACK("←ШГ"),
    BUTTON_RETURN("В/О"),
    BUTTON_PAUSE("С/П"),

    BUTTON_REGISTER_TO_X("П→X"),
    BUTTON_X_TO_REGISTER("X→П"),
    BUTTON_GOTO("БП"),
    BUTTON_SUBROUTINE("ПП"),

    BUTTON_SWAP("↔"),
    BUTTON_ENTER("В↑"),

    BUTTON_SIGN_TOGGLE("/-/"),
    BUTTON_WRITE_TO_MEMORY("ВП"),
    BUTTON_CLEAR("Сx"),

    BUTTON_A("a"),
    BUTTON_B("b"),
    BUTTON_C("c"),
    BUTTON_D("d"),
    BUTTON_E("e"),
}

fun MkButtonType.isDigit(): Boolean {
    return this in listOf(
        MkButtonType.BUTTON_0,
        MkButtonType.BUTTON_1,
        MkButtonType.BUTTON_2,
        MkButtonType.BUTTON_3,
        MkButtonType.BUTTON_4,
        MkButtonType.BUTTON_5,
        MkButtonType.BUTTON_6,
        MkButtonType.BUTTON_7,
        MkButtonType.BUTTON_8,
        MkButtonType.BUTTON_9
    )
}

fun MkButtonType.isOperation(): Boolean {
    return this in listOf(
        MkButtonType.BUTTON_MINUS,
        MkButtonType.BUTTON_DIVIDE,
        MkButtonType.BUTTON_PLUS,
        MkButtonType.BUTTON_MULTIPLY
    )
}

fun MkButtonType.isFunction(): Boolean {
    return this in listOf(
        MkButtonType.BUTTON_F,
        MkButtonType.BUTTON_K
    )
}

fun MkButtonType.isDot(): Boolean {
    return this == MkButtonType.BUTTON_DECIMAL_SEPARATOR
}

fun MkButtonType.isSignToggle(): Boolean {
    return this == MkButtonType.BUTTON_SIGN_TOGGLE
}

fun MkButtonType.isRegister(): Boolean {
    return this in listOf(
        MkButtonType.BUTTON_0,
        MkButtonType.BUTTON_1,
        MkButtonType.BUTTON_2,
        MkButtonType.BUTTON_3,
        MkButtonType.BUTTON_4,
        MkButtonType.BUTTON_5,
        MkButtonType.BUTTON_6,
        MkButtonType.BUTTON_7,
        MkButtonType.BUTTON_8,
        MkButtonType.BUTTON_9,
        MkButtonType.BUTTON_A,
        MkButtonType.BUTTON_B,
        MkButtonType.BUTTON_C,
        MkButtonType.BUTTON_D,
        MkButtonType.BUTTON_E
    )
}

fun MkButtonType.isRegisterNamed(): Boolean {
    return this in listOf(
        MkButtonType.BUTTON_A,
        MkButtonType.BUTTON_B,
        MkButtonType.BUTTON_C,
        MkButtonType.BUTTON_D,
        MkButtonType.BUTTON_E
    )
}