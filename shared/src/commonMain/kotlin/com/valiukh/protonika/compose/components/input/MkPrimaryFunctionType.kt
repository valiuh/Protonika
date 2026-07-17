package com.valiukh.protonika.compose.components.input

/**
 * Primary (F) function types for the main MK-61 layout.
 *
 * Values are taken from the yellow labels (жёл_т) printed above the keys.
 * UI component implementation will be added later.
 */
enum class MkPrimaryFunctionType(val text: String) {
    FUNCTION_X_LESS_THAN_ZERO("x<0"),
    FUNCTION_X_EQUALS_ZERO("x=0"),
    FUNCTION_X_GREATER_OR_EQUAL_ZERO("x≥0"),
    FUNCTION_X_NOT_EQUAL_ZERO("x≠0"),

    FUNCTION_L0("L0"),
    FUNCTION_L1("L1"),
    FUNCTION_L2("L2"),
    FUNCTION_L3("L3"),

    FUNCTION_SIN("sin"),
    FUNCTION_COS("cos"),
    FUNCTION_TG("tg"),
    FUNCTION_SQRT("√"),
    FUNCTION_RECIPROCAL("1/x"),

    FUNCTION_ARCSIN("sin⁻¹"),
    FUNCTION_ARCCOS("cos⁻¹"),
    FUNCTION_ARCTG("tg⁻¹"),
    FUNCTION_PI("π"),
    FUNCTION_X_SQUARED("x²"),

    FUNCTION_E_POWER_X("eˣ"),
    FUNCTION_LG("lg"),
    FUNCTION_LN("ln"),
    FUNCTION_X_POWER_Y("xʸ"),
    FUNCTION_LAST_X("Вx"),

    FUNCTION_TEN_POWER_X("10ˣ"),
    FUNCTION_STACK_RING("Ѻ"),
    FUNCTION_AUTO("АВТ"),
    FUNCTION_PROGRAM("ПРГ"),
    FUNCTION_CF("СF"),

    FUNCTION_F1("Ф1"),
    FUNCTION_F2("Ф2"),
    FUNCTION_F3("Ф3"),
    FUNCTION_F4("Ф4"),
    FUNCTION_F5("Ф5")
}
