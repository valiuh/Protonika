package com.valiukh.protonika.operational

import com.valiukh.protonika.compose.components.input.MkButtonType
import com.valiukh.protonika.compose.components.input.MkPrimaryFunctionType
import com.valiukh.protonika.compose.components.input.MkSecondaryFunctionType
import com.valiukh.protonika.virtualmachine.Command

/**
 * Maps a base key press (no active F/K modifier) to the virtual machine [Command] it
 * produces, or `null` when the key does not emit an instruction token on its own
 * (digits, register letters, F/K, decimal separator, sign toggle, ВП, step keys).
 */
internal fun MkButtonType.toCommand(): Command? = when (this) {
    MkButtonType.BUTTON_MINUS -> Command.SUB
    MkButtonType.BUTTON_DIVIDE -> Command.DIV
    MkButtonType.BUTTON_PLUS -> Command.ADD
    MkButtonType.BUTTON_MULTIPLY -> Command.MUL
    MkButtonType.BUTTON_SWAP -> Command.SWAP
    MkButtonType.BUTTON_ENTER -> Command.PUSH
    MkButtonType.BUTTON_PAUSE -> Command.STOP
    MkButtonType.BUTTON_RETURN -> Command.RTN
    MkButtonType.BUTTON_CLEAR -> Command.CLR
    MkButtonType.BUTTON_REGISTER_TO_X -> Command.MOVF
    MkButtonType.BUTTON_X_TO_REGISTER -> Command.MOVT
    MkButtonType.BUTTON_GOTO -> Command.GOTO
    MkButtonType.BUTTON_SUBROUTINE -> Command.GOSUB
    else -> null
}

/**
 * Maps a memory/jump key pressed while the K (secondary) modifier is active to its indirect
 * virtual machine [Command] (`К X→П`, `К П→X`, `К БП`, `К ПП`), or `null` for other keys.
 */
internal fun MkButtonType.toIndirectCommand(): Command? = when (this) {
    MkButtonType.BUTTON_X_TO_REGISTER -> Command.IMOVT
    MkButtonType.BUTTON_REGISTER_TO_X -> Command.IMOVF
    MkButtonType.BUTTON_GOTO -> Command.IGOTO
    MkButtonType.BUTTON_SUBROUTINE -> Command.IGOSUB
    else -> null
}

/**
 * Maps a primary (F / yellow) function to the virtual machine [Command] it produces, or
 * `null` for functions without a VM equivalent (АВТ, ПРГ, СF).
 */
internal fun MkPrimaryFunctionType.toCommand(): Command? = when (this) {
    MkPrimaryFunctionType.FUNCTION_X_LESS_THAN_ZERO -> Command.NEG
    MkPrimaryFunctionType.FUNCTION_X_EQUALS_ZERO -> Command.ZRO
    MkPrimaryFunctionType.FUNCTION_X_GREATER_OR_EQUAL_ZERO -> Command.NNG
    MkPrimaryFunctionType.FUNCTION_X_NOT_EQUAL_ZERO -> Command.NZR
    MkPrimaryFunctionType.FUNCTION_L0 -> Command.LOOP0
    MkPrimaryFunctionType.FUNCTION_L1 -> Command.LOOP1
    MkPrimaryFunctionType.FUNCTION_L2 -> Command.LOOP2
    MkPrimaryFunctionType.FUNCTION_L3 -> Command.LOOP3
    MkPrimaryFunctionType.FUNCTION_SIN -> Command.SIN
    MkPrimaryFunctionType.FUNCTION_COS -> Command.COS
    MkPrimaryFunctionType.FUNCTION_TG -> Command.TG
    MkPrimaryFunctionType.FUNCTION_SQRT -> Command.SQRT
    MkPrimaryFunctionType.FUNCTION_RECIPROCAL -> Command.FRAC
    MkPrimaryFunctionType.FUNCTION_ARCSIN -> Command.ARCSIN
    MkPrimaryFunctionType.FUNCTION_ARCCOS -> Command.ARCCOS
    MkPrimaryFunctionType.FUNCTION_ARCTG -> Command.ARCTG
    MkPrimaryFunctionType.FUNCTION_PI -> Command.PI
    MkPrimaryFunctionType.FUNCTION_X_SQUARED -> Command.SQR
    MkPrimaryFunctionType.FUNCTION_E_POWER_X -> Command.EXP
    MkPrimaryFunctionType.FUNCTION_LG -> Command.LG
    MkPrimaryFunctionType.FUNCTION_LN -> Command.NL
    MkPrimaryFunctionType.FUNCTION_X_POWER_Y -> Command.POW
    MkPrimaryFunctionType.FUNCTION_TEN_POWER_X -> Command.EXP10
    MkPrimaryFunctionType.FUNCTION_STACK_RING -> Command.SHIFT
    MkPrimaryFunctionType.FUNCTION_LAST_X -> Command.LASTX
    MkPrimaryFunctionType.FUNCTION_AUTO,
    MkPrimaryFunctionType.FUNCTION_PROGRAM,
    MkPrimaryFunctionType.FUNCTION_CF, MkPrimaryFunctionType.FUNCTION_F1,
    MkPrimaryFunctionType.FUNCTION_F2, MkPrimaryFunctionType.FUNCTION_F3,
    MkPrimaryFunctionType.FUNCTION_F4, MkPrimaryFunctionType.FUNCTION_F5 -> null
}

/**
 * Maps a conditional-jump key pressed while the K (secondary) modifier is active to its
 * indirect virtual machine [Command] (`К X<0`, `К X=0`, `К X≥0`, `К X≠0`), or `null`.
 */
internal fun MkPrimaryFunctionType.toIndirectCommand(): Command? = when (this) {
    MkPrimaryFunctionType.FUNCTION_X_LESS_THAN_ZERO -> Command.INEG
    MkPrimaryFunctionType.FUNCTION_X_EQUALS_ZERO -> Command.IZRO
    MkPrimaryFunctionType.FUNCTION_X_GREATER_OR_EQUAL_ZERO -> Command.INNG
    MkPrimaryFunctionType.FUNCTION_X_NOT_EQUAL_ZERO -> Command.INZR
    else -> null
}

/**
 * Maps a secondary (K / blue) math function to the virtual machine [Command] it produces.
 */
internal fun MkSecondaryFunctionType.toCommand(): Command? = when (this) {
    MkSecondaryFunctionType.FUNCTION_INTEGER_PART -> Command.FLR
    MkSecondaryFunctionType.FUNCTION_FRACTIONAL_PART -> Command.FRC
    MkSecondaryFunctionType.FUNCTION_MAX -> Command.MAX
    MkSecondaryFunctionType.FUNCTION_ABSOLUTE -> Command.ABS
    MkSecondaryFunctionType.FUNCTION_SIGN -> Command.SIGN
    MkSecondaryFunctionType.FUNCTION_NOP -> Command.NOP
    MkSecondaryFunctionType.FUNCTION_AND -> Command.AND
    MkSecondaryFunctionType.FUNCTION_OR -> Command.OR
    MkSecondaryFunctionType.FUNCTION_XOR -> Command.XOR
    MkSecondaryFunctionType.FUNCTION_INV -> Command.NOT
    MkSecondaryFunctionType.FUNCTION_HM_TO_DEG -> Command.HM_TO_DEG
    MkSecondaryFunctionType.FUNCTION_DEG_TO_HM -> Command.DEG_TO_HM
    MkSecondaryFunctionType.FUNCTION_HMS_TO_DEG -> Command.HMS_TO_DEG
    MkSecondaryFunctionType.FUNCTION_DEG_TO_HMS -> Command.DEG_TO_HMS
    MkSecondaryFunctionType.FUNCTION_RANDOM -> Command.RANDOM
    MkSecondaryFunctionType.FUNCTION_CURRENCY_SIGN -> null
    MkSecondaryFunctionType.FUNCTION_SECTION_SIGN -> null
    MkSecondaryFunctionType.FUNCTION_SUPERSET -> null
    MkSecondaryFunctionType.FUNCTION_SUBSET -> null
    MkSecondaryFunctionType.FUNCTION_INFINITY -> null
}
