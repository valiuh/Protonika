package com.valiukh.protonika.compose.components.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valiukh.protonika.compose.theme.AppTheme

/**
 * A single parsed piece of an instruction string.
 *
 * An instruction is split around [cursor] tokens so the cursor can be rendered
 * inline at its exact position without losing surrounding characters.
 */
sealed interface InstructionSegment {
    data class Text(val value: String) : InstructionSegment
    data object Cursor : InstructionSegment
}

/**
 * Splits [instruction] into ordered [InstructionSegment]s around every occurrence
 * of [cursorToken].
 *
 * The function is pure and position-preserving:
 * - no token           -> a single [InstructionSegment.Text];
 * - token at any edge  -> a [InstructionSegment.Cursor] on that side;
 * - token as the whole string -> a single [InstructionSegment.Cursor];
 * - multiple tokens (malformed) -> every occurrence is emitted, nothing is dropped;
 * - empty string       -> an empty list.
 */
fun parseInstruction(
    instruction: String,
    cursorToken: String = cursor,
): List<InstructionSegment> {
    if (cursorToken.isEmpty()) {
        return if (instruction.isEmpty()) emptyList() else listOf(InstructionSegment.Text(instruction))
    }

    val segments = mutableListOf<InstructionSegment>()
    var index = 0
    while (true) {
        val found = instruction.indexOf(cursorToken, startIndex = index)
        if (found < 0) {
            if (index < instruction.length) {
                segments += InstructionSegment.Text(instruction.substring(index))
            }
            break
        }
        if (found > index) {
            segments += InstructionSegment.Text(instruction.substring(index, found))
        }
        segments += InstructionSegment.Cursor
        index = found + cursorToken.length
    }
    return segments
}

/**
 * A read-only, state-hoisted program listing editor.
 *
 * Renders every element of [instructions] in order as `index: text`, interpreting
 * the [cursor] token as an inline insertion caret. The component never mutates
 * [instructions]; it only emits user intent through callbacks so the owner
 * (a ViewModel / parent state holder) can update the immutable list and pass a
 * new instance back.
 *
 * @param instructions ordered, immutable program instructions.
 * @param isEditing whether editing mode is active (enables row selection).
 * @param onEditingModeChanged invoked with the requested editing state when the
 *   top control is toggled.
 * @param selectedIndex the currently selected instruction row, or `null`.
 * @param onInstructionSelected invoked with the tapped row index while editing.
 */
@Composable
fun MkProgramEditor(
    modifier: Modifier = Modifier,
    instructions: List<String>,
    isEditing: Boolean = false,
    selectedIndex: Int? = null,
    onInstructionSelected: (Int) -> Unit = {},
    onEditingModeChanged: (Boolean) -> Unit = {},
) {
    val colors = AppTheme.colors
    val listState = rememberLazyListState()

    // Width (in characters) of the widest line number, so the gutter stays aligned
    // even when the program reaches multiple digits.
    val lineNumberWidth = remember(instructions.size) {
        instructions.lastIndex.coerceAtLeast(0).toString().length
    }

    // Keep the row that owns the cursor (or the last row) visible as the program grows.
    val cursorIndex = remember(instructions) {
        instructions.indexOfLast { it.contains(cursor) }
    }
    LaunchedEffect(instructions.size, cursorIndex) {
        val target = if (cursorIndex >= 0) cursorIndex else instructions.lastIndex
        if (target >= 0) listState.animateScrollToItem(target)
    }

    Column(
        modifier = modifier
            .background(colors.instrumental)
            .border(width = 1.dp, color = colors.instrumentalInverted),
    ) {
        HorizontalDivider(thickness = 1.dp, color = colors.instrumentalInverted)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
        ) {
            itemsIndexed(
                items = instructions,
                key = { index, _ -> index },
            ) { index, instruction ->
                val isCursorOnly = instruction == cursor
                InstructionRow(
                    lineNumber = index.toString().padStart(lineNumberWidth) + ":",
                    instruction = instruction,
                    isSelected = isEditing && selectedIndex == index,
                    isSelectable = isEditing && !isCursorOnly,
                    onClick = { onInstructionSelected(index) },
                )
            }
        }
    }
}

@Composable
private fun InstructionRow(
    lineNumber: String,
    instruction: String,
    isSelected: Boolean,
    isSelectable: Boolean,
    onClick: () -> Unit,
) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isSelectable) Modifier.clickable(onClick = onClick) else Modifier)
            .then(if (isSelected) Modifier.background(colors.secondaryFunctionalText.copy(alpha = 0.1f)) else Modifier)
            .then(if (instruction.contains(cursor)) Modifier.background(colors.instrumentalInverted.copy(alpha = 0.1f)) else Modifier)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = lineNumber,
            color = colors.instrumentalInverted.copy(alpha = 0.7f),
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = instruction.replace(cursor, ""),
            color = colors.instrumentalInverted,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun instructionAnnotatedString(instruction: String): AnnotatedString {
    val colors = AppTheme.colors
    return buildAnnotatedString {
        parseInstruction(instruction).forEach { segment ->
            when (segment) {
                is InstructionSegment.Text -> append(segment.value)
                InstructionSegment.Cursor -> withStyle(
                    SpanStyle(background = colors.instrumentalInverted.copy(alpha = 0.25f)),
                ) {
                    append(cursor)
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun MkProgramEditorPreview() {
    AppTheme {
        MkProgramEditor(
            modifier = Modifier.height(240.dp),
            instructions = listOf(
                "LOAD A",
                "ADD B",
                cursor,
            ),
        )
    }
}

@PreviewLightDark
@Composable
fun MkProgramEditorInlineCursorPreview() {
    AppTheme {
        MkProgramEditor(
            modifier = Modifier.height(240.dp),
            instructions = listOf(
                "LOAD A",
                "ADD $cursor",
            ),
        )
    }
}

@PreviewLightDark
@Composable
fun MkProgramEditorEditingPreview() {
    AppTheme {
        MkProgramEditor(
            modifier = Modifier.height(240.dp),
            instructions = listOf(
                "LOAD A",
                "ADD B",
                "MUL C",
                cursor,
            ),
            isEditing = true,
            selectedIndex = 1,
        )
    }
}