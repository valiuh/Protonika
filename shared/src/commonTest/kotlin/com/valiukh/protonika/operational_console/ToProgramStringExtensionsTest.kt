package com.valiukh.protonika.operational_console

import com.valiukh.protonika.operational.toProgramString
import kotlin.test.Test
import kotlin.test.assertEquals


class ToProgramStringExtensionsTest {


    @Test
    fun `test to program string`() {
        val script = """
                2
                B↑
                3
                +
                X=0
                10
                X→П 8
                B↑
                6
                -
                B↑
                2
                +
                С/П
            """.trimIndent()
        val instructions = listOf (
            "2",
            "B↑",
            "3",
            "+",
            "X=0",
            "10",
            "X→П 8",
            "B↑",
            "6",
            "-",
            "B↑",
            "2",
            "+",
            "С/П",
            "F_"
        )
        val programScript = instructions.toProgramString()
        assertEquals(script, programScript)
    }
}
