package com.valiukh.protonika.compose.components.input

/**
 * Secondary (K) function types for the main MK-61 layout.
 *
 * Values are taken from the blue labels (син_т) printed above the keys.
 * UI component implementation will be added later.
 */
enum class MkSecondaryFunctionType(val text: String) {
    FUNCTION_INTEGER_PART("[x]"),
    FUNCTION_FRACTIONAL_PART("{x}"),
    FUNCTION_MAX("max"),

    FUNCTION_ABSOLUTE("|x|"),
    FUNCTION_SIGN("ЗН"),
    FUNCTION_HM_TO_DEG("°←'"),
    FUNCTION_DEG_TO_HM("°→'"),
    FUNCTION_HMS_TO_DEG("°←''\""),
    FUNCTION_DEG_TO_HMS("°→''\""),

    FUNCTION_RANDOM("СЧ"),

    FUNCTION_NOP("НОП"),
    FUNCTION_AND("∧"),
    FUNCTION_OR("∨"),
    FUNCTION_XOR("⊕"),
    FUNCTION_INV("ИНВ"),

    FUNCTION_CURRENCY_SIGN("¤"),
    FUNCTION_SECTION_SIGN("§"),
    FUNCTION_SUPERSET("⊃"),
    FUNCTION_SUBSET("⊂"),
    FUNCTION_INFINITY("∞"),
}
