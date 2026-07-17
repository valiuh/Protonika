package com.valiukh.protonika.compose.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.valiukh.protonika.compose.theme.AppTheme
import com.valiukh.protonika.compose.theme.SmokyUmber
import com.valiukh.protonika.compose.theme.White

@Composable
fun MkButton(
    modifier: Modifier = Modifier,
    type: MkButtonType,
    onClick: (type: MkButtonType) -> Unit = {},
) {
    val colors = AppTheme.colors

    val backgroundColor = when(type) {
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
        MkButtonType.BUTTON_DECIMAL_SEPARATOR,
        MkButtonType.BUTTON_MINUS,
        MkButtonType.BUTTON_DIVIDE,
        MkButtonType.BUTTON_PLUS,
        MkButtonType.BUTTON_MULTIPLY,
        MkButtonType.BUTTON_SIGN_TOGGLE,
        MkButtonType.BUTTON_WRITE_TO_MEMORY,
        MkButtonType.BUTTON_SWAP,
        MkButtonType.BUTTON_ENTER,
        MkButtonType.BUTTON_A,
        MkButtonType.BUTTON_B,
        MkButtonType.BUTTON_C,
        MkButtonType.BUTTON_D,
        MkButtonType.BUTTON_E -> colors.primaryOperational

        MkButtonType.BUTTON_STEP_FORWARD,
        MkButtonType.BUTTON_STEP_BACK,
        MkButtonType.BUTTON_RETURN,
        MkButtonType.BUTTON_PAUSE,
        MkButtonType.BUTTON_REGISTER_TO_X,
        MkButtonType.BUTTON_X_TO_REGISTER,
        MkButtonType.BUTTON_GOTO,
        MkButtonType.BUTTON_SUBROUTINE -> colors.secondaryOperational

        MkButtonType.BUTTON_F -> colors.primaryFunctional

        MkButtonType.BUTTON_K -> colors.secondaryFunctional

        MkButtonType.BUTTON_CLEAR -> colors.instrumentalFunctional
    }

    val textColor = when(type) {
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
        MkButtonType.BUTTON_DECIMAL_SEPARATOR,
        MkButtonType.BUTTON_MINUS,
        MkButtonType.BUTTON_DIVIDE,
        MkButtonType.BUTTON_PLUS,
        MkButtonType.BUTTON_MULTIPLY,
        MkButtonType.BUTTON_SIGN_TOGGLE,
        MkButtonType.BUTTON_WRITE_TO_MEMORY,
        MkButtonType.BUTTON_SWAP,
        MkButtonType.BUTTON_ENTER,
        MkButtonType.BUTTON_A,
        MkButtonType.BUTTON_B,
        MkButtonType.BUTTON_C,
        MkButtonType.BUTTON_D,
        MkButtonType.BUTTON_E -> SmokyUmber

        MkButtonType.BUTTON_STEP_FORWARD,
        MkButtonType.BUTTON_STEP_BACK,
        MkButtonType.BUTTON_RETURN,
        MkButtonType.BUTTON_PAUSE,
        MkButtonType.BUTTON_REGISTER_TO_X,
        MkButtonType.BUTTON_X_TO_REGISTER,
        MkButtonType.BUTTON_GOTO,
        MkButtonType.BUTTON_SUBROUTINE,
        MkButtonType.BUTTON_K,
        MkButtonType.BUTTON_CLEAR -> White

        MkButtonType.BUTTON_F -> SmokyUmber
    }

    val shape = RoundedCornerShape(5.dp)

    Box(
        modifier = modifier
            .size(
                width = 52.dp,
                height = 36.dp
            )
            .clip(shape)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = colors.borderOperational,
                shape = shape
            )
            .clickable(onClick = { onClick(type) }),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = type.text,
            color = textColor,
            fontWeight = FontWeight.Bold,
        )
    }
}

@PreviewLightDark
@Composable
fun OperationalButtonPreview() {
    AppTheme {
        Column(
            Modifier.background(color = AppTheme.colors.instrumental)
        ) {
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_0,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_1,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_2,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_3,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_4,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_5,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_6,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_7,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_8,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_9,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_DECIMAL_SEPARATOR,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_MINUS,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_PLUS,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_DIVIDE,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_MULTIPLY,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_SIGN_TOGGLE,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_WRITE_TO_MEMORY,
                onClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
fun MemoryButtonPreview() {
    AppTheme {
        Column(
            Modifier.background(color = AppTheme.colors.instrumental)
        ) {
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_STEP_FORWARD,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_STEP_BACK,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_RETURN,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_PAUSE,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_REGISTER_TO_X,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_X_TO_REGISTER,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_GOTO,
                onClick = {},
            )
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_SUBROUTINE,
                onClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
fun FunctionalButtonPreview() {
    AppTheme {
        Column(
            Modifier.background(color = AppTheme.colors.instrumental)
        ) {
            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_F,
                onClick = {},
            )

            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_K,
                onClick = {},
            )

            MkButton(
                modifier = Modifier.padding(20.dp),
                type = MkButtonType.BUTTON_CLEAR,
                onClick = {},
            )
        }
    }
}
