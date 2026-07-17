package com.valiukh.protonika.compose.components.editor

import kotlin.test.Test
import kotlin.test.assertEquals

class InstructionParsingTest {

    @Test
    fun noCursorProducesSingleTextSegment() {
        assertEquals(
            listOf(InstructionSegment.Text("LOAD A")),
            parseInstruction("LOAD A"),
        )
    }

    @Test
    fun cursorAtEndStaysInline() {
        assertEquals(
            listOf(InstructionSegment.Text("ADD "), InstructionSegment.Cursor),
            parseInstruction("ADD $cursor"),
        )
    }

    @Test
    fun cursorAtBeginning() {
        assertEquals(
            listOf(InstructionSegment.Cursor, InstructionSegment.Text("ADD")),
            parseInstruction("${cursor}ADD"),
        )
    }

    @Test
    fun cursorInTheMiddle() {
        assertEquals(
            listOf(
                InstructionSegment.Text("A"),
                InstructionSegment.Cursor,
                InstructionSegment.Text("B"),
            ),
            parseInstruction("A${cursor}B"),
        )
    }

    @Test
    fun cursorAsEntireInstruction() {
        assertEquals(
            listOf(InstructionSegment.Cursor),
            parseInstruction(cursor),
        )
    }

    @Test
    fun emptyInstructionProducesNoSegments() {
        assertEquals(
            emptyList(),
            parseInstruction(""),
        )
    }

    @Test
    fun multipleCursorsAreAllPreserved() {
        assertEquals(
            listOf(
                InstructionSegment.Cursor,
                InstructionSegment.Text(" "),
                InstructionSegment.Cursor,
            ),
            parseInstruction("$cursor $cursor"),
        )
    }
}
