package com.valiukh.protonika.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valiukh.protonika.compose.theme.AppTheme

private const val Separator = "░░░░░░░░░░░░░░░░░░"

@Composable
fun MkOutputConsole(
    modifier: Modifier = Modifier,
    state: MkOutputConsoleState = MkOutputConsoleState(),
) {
    val colors = AppTheme.colors

    Column(
        modifier = modifier
            .background(colors.instrumental)
            .border(width = 1.dp, color = colors.instrumentalInverted)
    ) {
        ConsoleLine("Stack:")
        ConsoleLine("X: ${state.registerX}")
        ConsoleLine("Y: ${state.registerY}")
        ConsoleLine("Z: ${state.registerZ}")
        ConsoleLine("T: ${state.registerT}")
        ConsoleLine(Separator)

        ConsoleLine("Previous Result:")
        ConsoleLine("X1: ${state.registerX1}")
        ConsoleLine(Separator)

        ConsoleLine("Registers:")
        val leftColumn = listOf(
            "0: ${state.register0}",
            "1: ${state.register1}",
            "2: ${state.register2}",
            "3: ${state.register3}",
            "",
            "4: ${state.register4}",
            "5: ${state.register5}",
            "6: ${state.register6}",
        )
        val rightColumn = listOf(
            "7: ${state.register7}",
            "8: ${state.register8}",
            "9: ${state.register9}",
            "a: ${state.registerA}",
            "b: ${state.registerB}",
            "c: ${state.registerC}",
            "d: ${state.registerD}",
            "e: ${state.registerE}",
        )
        val gap = 5
        val leftColumnWidth = leftColumn.maxOf { it.length }

        leftColumn.zip(rightColumn).forEach { (left, right) ->
            ConsoleLine(left.padEnd(leftColumnWidth + gap) + right)
        }
    }
}

@Composable
private fun ConsoleBorderTitle(text: String) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = colors.instrumentalInverted,
        )
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp),
            color = colors.instrumentalInverted,
            fontFamily = FontFamily.Monospace,
            fontSize = 8.sp,
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = colors.instrumentalInverted,
        )
    }
}

@Composable
private fun ConsoleLine(text: String) {
    val colors = AppTheme.colors
    Text(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        text = text,
        color = colors.instrumentalInverted,
        fontFamily = FontFamily.Monospace,
        fontSize = 14.sp,
    )
}

@PreviewLightDark
@Composable
fun MkOutputConsolePreview() {
    AppTheme {
        MkOutputConsole(
            state = MkOutputConsoleState(
                registerX = 12.3,
                registerY = -42.0,
                registerZ = 7.0,
                registerT = 0.0,
                registerX1 = 314159.0,
                register0 = 100.0,
                register1 = 101.0,
                register2 = 102.0,
                register3 = 103.0,
                register4 = 104.0,
                register5 = 105.0,
                register6 = 106.0,
                register7 = 107.0,
                register8 = 108.0,
                register9 = 109.0,
                registerA = 110.0,
                registerB = 111.0,
                registerC = 112.0,
                registerD = 113.0,
                registerE = 114.0,
            ),
        )
    }
}

data class MkOutputConsoleState(
    val registerX: Double = 0.0,
    val registerY: Double = 0.0,
    val registerZ: Double = 0.0,
    val registerT: Double = 0.0,
    val registerX1: Double = 0.0,
    val register0: Double = 0.0,
    val register1: Double = 0.0,
    val register2: Double = 0.0,
    val register3: Double = 0.0,
    val register4: Double = 0.0,
    val register5: Double = 0.0,
    val register6: Double = 0.0,
    val register7: Double = 0.0,
    val register8: Double = 0.0,
    val register9: Double = 0.0,
    val registerA: Double = 0.0,
    val registerB: Double = 0.0,
    val registerC: Double = 0.0,
    val registerD: Double = 0.0,
    val registerE: Double = 0.0,
)