package com.valiukh.protonika.compose.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.valiukh.protonika.compose.theme.AppTheme

data class Key(
    val type: MkButtonType,
    val primaryType: MkPrimaryFunctionType? = null,
    val secondaryType: MkSecondaryFunctionType? = null,
)

@Composable
fun MkButtonSurface(
    modifier: Modifier = Modifier,
    isPrimaryFunctionActive: Boolean = false,
    isSecondaryFunctionActive: Boolean = false,
    onClick: (
        type: MkButtonType,
        primaryType: MkPrimaryFunctionType?,
        secondaryType: MkSecondaryFunctionType?
    ) -> Unit = { _, _, _ -> },
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        buttons.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                row.forEach { key ->
                    MkButtonPlace(
                        isPrimaryFunctionActive = isPrimaryFunctionActive,
                        isSecondaryFunctionActive = isSecondaryFunctionActive,
                        type = key.type,
                        primaryType = key.primaryType,
                        secondaryType = key.secondaryType,
                        onClick = onClick,
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun MkButtonSurfacePreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(AppTheme.colors.instrumental)
                .padding(8.dp),
        ) {
            MkButtonSurface()
        }
    }
}

val buttons = listOf(
    listOf(
        Key(
            type = MkButtonType.BUTTON_F
        ),
        Key(
            type = MkButtonType.BUTTON_STEP_FORWARD,
            primaryType = MkPrimaryFunctionType.FUNCTION_X_LESS_THAN_ZERO,
        ),
        Key(
            type = MkButtonType.BUTTON_STEP_BACK,
            primaryType = MkPrimaryFunctionType.FUNCTION_X_EQUALS_ZERO,
        ),
        Key(
            type = MkButtonType.BUTTON_RETURN,
            primaryType = MkPrimaryFunctionType.FUNCTION_X_GREATER_OR_EQUAL_ZERO,
        ),
        Key(
            type = MkButtonType.BUTTON_PAUSE,
            primaryType = MkPrimaryFunctionType.FUNCTION_X_NOT_EQUAL_ZERO,
        ),
    ),
    listOf(
        Key(
            type = MkButtonType.BUTTON_K
        ),
        Key(
            type = MkButtonType.BUTTON_REGISTER_TO_X,
            primaryType = MkPrimaryFunctionType.FUNCTION_L0,
        ),
        Key(
            type = MkButtonType.BUTTON_X_TO_REGISTER,
            primaryType = MkPrimaryFunctionType.FUNCTION_L1,
        ),
        Key(
            type = MkButtonType.BUTTON_GOTO,
            primaryType = MkPrimaryFunctionType.FUNCTION_L2,
        ),
        Key(
            type = MkButtonType.BUTTON_SUBROUTINE,
            primaryType = MkPrimaryFunctionType.FUNCTION_L3,
        ),
    ),
    listOf(
        Key(
            type = MkButtonType.BUTTON_7,
            primaryType = MkPrimaryFunctionType.FUNCTION_SIN,
            secondaryType = MkSecondaryFunctionType.FUNCTION_INTEGER_PART,
        ),
        Key(
            type = MkButtonType.BUTTON_8,
            primaryType = MkPrimaryFunctionType.FUNCTION_COS,
            secondaryType = MkSecondaryFunctionType.FUNCTION_FRACTIONAL_PART,
        ),
        Key(
            type = MkButtonType.BUTTON_9,
            primaryType = MkPrimaryFunctionType.FUNCTION_TG,
            secondaryType = MkSecondaryFunctionType.FUNCTION_MAX,
        ),
        Key(
            type = MkButtonType.BUTTON_MINUS,
            primaryType = MkPrimaryFunctionType.FUNCTION_SQRT,
        ),
        Key(
            type = MkButtonType.BUTTON_DIVIDE,
            primaryType = MkPrimaryFunctionType.FUNCTION_RECIPROCAL,
        ),
    ),
    listOf(
        Key(
            type = MkButtonType.BUTTON_4,
            primaryType = MkPrimaryFunctionType.FUNCTION_ARCSIN,
            secondaryType = MkSecondaryFunctionType.FUNCTION_ABSOLUTE,
        ),
        Key(
            type = MkButtonType.BUTTON_5,
            primaryType = MkPrimaryFunctionType.FUNCTION_ARCCOS,
            secondaryType = MkSecondaryFunctionType.FUNCTION_SIGN,
        ),
        Key(
            type = MkButtonType.BUTTON_6,
            primaryType = MkPrimaryFunctionType.FUNCTION_ARCTG,
            secondaryType = MkSecondaryFunctionType.FUNCTION_HM_TO_DEG,
        ),
        Key(
            type = MkButtonType.BUTTON_PLUS,
            primaryType = MkPrimaryFunctionType.FUNCTION_PI,
            secondaryType = MkSecondaryFunctionType.FUNCTION_DEG_TO_HM,
        ),
        Key(
            type = MkButtonType.BUTTON_MULTIPLY,
            primaryType = MkPrimaryFunctionType.FUNCTION_X_SQUARED,
        ),
    ),
    listOf(
        Key(
            type = MkButtonType.BUTTON_1,
            primaryType = MkPrimaryFunctionType.FUNCTION_E_POWER_X,
        ),
        Key(
            type = MkButtonType.BUTTON_2,
            primaryType = MkPrimaryFunctionType.FUNCTION_LG,
        ),
        Key(
            type = MkButtonType.BUTTON_3,
            primaryType = MkPrimaryFunctionType.FUNCTION_LN,
            secondaryType = MkSecondaryFunctionType.FUNCTION_HMS_TO_DEG,
        ),
        Key(
            type = MkButtonType.BUTTON_SWAP,
            primaryType = MkPrimaryFunctionType.FUNCTION_X_POWER_Y,
            secondaryType = MkSecondaryFunctionType.FUNCTION_DEG_TO_HMS,
        ),
        Key(
            type = MkButtonType.BUTTON_ENTER,
            primaryType = MkPrimaryFunctionType.FUNCTION_LAST_X,
            secondaryType = MkSecondaryFunctionType.FUNCTION_RANDOM,
        ),
    ),
    listOf(
        Key(
            type = MkButtonType.BUTTON_0,
            primaryType = MkPrimaryFunctionType.FUNCTION_TEN_POWER_X,
            secondaryType = MkSecondaryFunctionType.FUNCTION_NOP,
        ),
        Key(
            type = MkButtonType.BUTTON_DECIMAL_SEPARATOR,
            primaryType = MkPrimaryFunctionType.FUNCTION_STACK_RING,
            secondaryType = MkSecondaryFunctionType.FUNCTION_AND,
        ),
        Key(
            type = MkButtonType.BUTTON_SIGN_TOGGLE,
            primaryType = MkPrimaryFunctionType.FUNCTION_AUTO,
            secondaryType = MkSecondaryFunctionType.FUNCTION_OR,
        ),
        Key(
            type = MkButtonType.BUTTON_WRITE_TO_MEMORY,
            primaryType = MkPrimaryFunctionType.FUNCTION_PROGRAM,
            secondaryType = MkSecondaryFunctionType.FUNCTION_XOR,
        ),
        Key(
            type = MkButtonType.BUTTON_CLEAR,
            primaryType = MkPrimaryFunctionType.FUNCTION_CF,
            secondaryType = MkSecondaryFunctionType.FUNCTION_INV,
        ),
    ),
    listOf(
        Key(
            type = MkButtonType.BUTTON_A,
            primaryType = MkPrimaryFunctionType.FUNCTION_F1,
            secondaryType =MkSecondaryFunctionType.FUNCTION_INFINITY,
        ),
        Key(
            type = MkButtonType.BUTTON_B,
            primaryType = MkPrimaryFunctionType.FUNCTION_F2,
            secondaryType = MkSecondaryFunctionType.FUNCTION_SECTION_SIGN,
        ),
        Key(
            type = MkButtonType.BUTTON_C,
            primaryType = MkPrimaryFunctionType.FUNCTION_F3,
            secondaryType = MkSecondaryFunctionType.FUNCTION_SUPERSET,
        ),
        Key(
            type = MkButtonType.BUTTON_D,
            primaryType = MkPrimaryFunctionType.FUNCTION_F4,
            secondaryType = MkSecondaryFunctionType.FUNCTION_SUBSET,
        ),
        Key(
            type = MkButtonType.BUTTON_E,
            primaryType = MkPrimaryFunctionType.FUNCTION_F5,
            secondaryType = MkSecondaryFunctionType.FUNCTION_CURRENCY_SIGN,
        ),
    )
)