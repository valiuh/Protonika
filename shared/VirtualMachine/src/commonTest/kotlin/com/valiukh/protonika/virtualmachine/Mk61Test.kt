package com.valiukh.protonika.virtualmachine

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Mk61Test {

    private lateinit var calculator: Mk61

    private fun readScript(scriptName: String): String = loadTestScript(scriptName)

    private fun assertRegistersEqual(
        expected: List<Double>,
        actual: List<Double>,
        absoluteTolerance: Double = 1e-12,
    ) {
        assertEquals(expected.size, actual.size)
        expected.forEachIndexed { index, value ->
            assertEquals(value, actual[index], absoluteTolerance)
        }
    }

    @BeforeTest
    fun init() {
        calculator = Mk61()
    }

    @Test
    fun `Scenario - clear`() {
        val script = "СX \n С/П"
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printX())
    }

    @Test
    fun `Scenario - В↑`() {
        val script1 = "4 \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(4.0, calculator.printX())
        assertEquals(listOf(4.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        val script2 = "5 \n С/П"
        calculator.uploadProgram(script2)
        calculator.calculate()
        assertEquals(5.0, calculator.printX())
        assertEquals(listOf(5.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        val script3 = "B↑ \n 6 \n С/П"
        calculator.uploadProgram(script3)
        calculator.calculate()
        assertEquals(6.0, calculator.printX())
        assertEquals(listOf(6.0, 5.0, 0.0, 0.0), calculator.printRegisters())

        val script4 = "B↑ \n С/П"
        calculator.uploadProgram(script4)
        calculator.calculate()
        assertEquals(6.0, calculator.printX())
        assertEquals(listOf(6.0, 6.0, 5.0, 0.0), calculator.printRegisters())

        val script5 = "7 \n B↑ \n С/П"
        calculator.uploadProgram(script5)
        calculator.calculate()
        assertEquals(7.0, calculator.printX())
        assertEquals(listOf(7.0, 7.0, 6.0, 5.0), calculator.printRegisters())

        val script6 = "8 \n B↑ \n С/П"
        calculator.uploadProgram(script6)
        calculator.calculate()
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 8.0, 7.0, 6.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ↻`() {
        val script1 = "4 \n B↑ \n 5 \n B↑ \n 6 \n B↑ \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(listOf(6.0, 6.0, 5.0, 4.0), calculator.printRegisters())

        val script2 = "↻ \n С/П"
        calculator.uploadProgram(script2)
        calculator.calculate()
        assertEquals(6.0, calculator.printX1())
        assertEquals(listOf(6.0, 5.0, 4.0, 6.0), calculator.printRegisters())

        val script3 = "↻ \n С/П"
        calculator.uploadProgram(script3)
        calculator.calculate()
        assertEquals(6.0, calculator.printX1())
        assertEquals(listOf(5.0, 4.0, 6.0, 6.0), calculator.printRegisters())

        val script4 = "↻ \n ↻ \n С/П"
        calculator.uploadProgram(script4)
        calculator.calculate()
        assertEquals(4.0, calculator.printX1())
        assertEquals(listOf(6.0, 6.0, 5.0, 4.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ↔`() {
        val script1 = "4 \n B↑ \n 5 \n B↑ \n 6 \n B↑ \n 7 \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(listOf(7.0, 6.0, 5.0, 4.0), calculator.printRegisters())

        val script2 = "↔ \n С/П"
        calculator.uploadProgram(script2)
        calculator.calculate()
        assertEquals(7.0, calculator.printX1())
        assertEquals(listOf(6.0, 7.0, 5.0, 4.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - +`() {
        val script1 = "5 \n B↑ \n 4 \n + \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(4.0, calculator.printX1())
        assertEquals(9.0, calculator.printX())
        assertEquals(listOf(9.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        val script2 = "3 \n B↑ \n 2 \n B↑ \n 1 \n B↑ \n 4 \n + \n С/П"
        calculator.uploadProgram(script2)
        calculator.calculate()
        assertEquals(4.0, calculator.printX1())
        assertEquals(5.0, calculator.printX())
        assertEquals(listOf(5.0, 2.0, 3.0, 3.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - -`() {
        val script1 = "5 \n B↑ \n 4 \n - \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(4.0, calculator.printX1())
        assertEquals(1.0, calculator.printX())
        assertEquals(listOf(1.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        val script2 = "3 \n B↑ \n 2 \n B↑ \n 1 \n B↑ \n 4 \n - \n С/П"
        calculator.uploadProgram(script2)
        calculator.calculate()
        assertEquals(4.0, calculator.printX1())
        assertEquals(-3.0, calculator.printX())
        assertEquals(listOf(-3.0, 2.0, 3.0, 3.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ×`() {
        val script1 = "3 \n B↑ \n 2 \n × \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.0, calculator.printX1())
        assertEquals(6.0, calculator.printX())
        assertEquals(listOf(6.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        val script2 = "3 \n B↑ \n 2 \n B↑ \n 1 \n B↑ \n 4 \n × \n С/П"
        calculator.uploadProgram(script2)
        calculator.calculate()
        assertEquals(4.0, calculator.printX1())
        assertEquals(4.0, calculator.printX())
        assertEquals(listOf(4.0, 2.0, 3.0, 3.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ÷`() {
        val script1 = "6 \n B↑ \n 2 \n ÷ \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.0, calculator.printX1())
        assertEquals(3.0, calculator.printX())
        assertEquals(listOf(3.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        val script2 = "3 \n B↑ \n 2 \n B↑ \n 6 \n B↑ \n 2 \n ÷ \n С/П"
        calculator.uploadProgram(script2)
        calculator.calculate()
        assertEquals(2.0, calculator.printX1())
        assertEquals(3.0, calculator.printX())
        assertEquals(listOf(3.0, 2.0, 3.0, 3.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - 10^x`() {
        val script1 = "2 \n 10^x \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.0, calculator.printX1())
        assertEquals(100.0, calculator.printX())
        assertEquals(listOf(100.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - e^x`() {
        val script1 = "2 \n e^x \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.0, calculator.printX1())
        assertEquals(7.3890560989306495, calculator.printX())
        assertEquals(listOf(7.3890560989306495, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - lg`() {
        val script1 = "2 \n lg \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.0, calculator.printX1())
        assertEquals(0.3010299956639812, calculator.printX())
        assertEquals(listOf(0.3010299956639812, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ln`() {
        val script1 = "2 \n ln \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.0, calculator.printX1())
        assertEquals(0.6931471805599453, calculator.printX())
        assertEquals(listOf(0.6931471805599453, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - sin`() {
        val script1 = "30 \n sin \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(30.0, calculator.printX1())
        assertEquals(0.49999999999999994, calculator.printX())
        assertEquals(listOf(0.49999999999999994, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - cos`() {
        val script1 = "30 \n cos \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(30.0, calculator.printX1())
        assertEquals(0.8660254037844387, calculator.printX())
        assertEquals(listOf(0.8660254037844387, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - tg`() {
        val script1 = "30 \n tg \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(30.0, calculator.printX1())
        assertEquals(0.5773502691896257, calculator.printX(), 1e-12)
        assertRegistersEqual(listOf(0.5773502691896257, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - sin^-1`() {
        val script1 = "0.5 \n sin^-1 \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(0.5, calculator.printX1())
        assertEquals(30.000000000000004, calculator.printX(), 1e-12)
        assertRegistersEqual(listOf(30.000000000000004, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - cos^-1`() {
        val script1 = "0.5 \n cos^-1 \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(0.5, calculator.printX1())
        assertEquals(60.00000000000001, calculator.printX(), 1e-12)
        assertRegistersEqual(listOf(60.00000000000001, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - tg^-1`() {
        val script1 = "0.5 \n tg^-1 \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(0.5, calculator.printX1())
        assertEquals(26.56505117707799, calculator.printX())
        assertEquals(listOf(26.56505117707799, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - √`() {
        val script1 = "4 \n √ \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(4.0, calculator.printX1())
        assertEquals(2.0, calculator.printX())
        assertEquals(listOf(2.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - 1 div x`() {
        val script1 = "4 \n 1/x \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(4.0, calculator.printX1())
        assertEquals(0.25, calculator.printX())
        assertEquals(listOf(0.25, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - x^2`() {
        val script1 = "4 \n x^2 \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(4.0, calculator.printX1())
        assertEquals(16.0, calculator.printX())
        assertEquals(listOf(16.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - x^y`() {
        val script1 = "3 \n B↑ \n 2 \n x^y \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 3.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - π`() {
        val script1 = "π \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(3.141592653589793, calculator.printX())
        assertEquals(listOf(3.141592653589793, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - e`() {
        val script1 = "e \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.718281828459045, calculator.printX())
        assertEquals(listOf(2.718281828459045, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - module of x`() {
        val script1 = "2.99 \n [x] \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.99, calculator.printX1())
        assertEquals(2.0, calculator.printX())
        assertEquals(listOf(2.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - fractional part of x`() {
        val script1 = "2.99 \n {x} \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.99, calculator.printX1())
        assertEquals(0.9900000000000002, calculator.printX())
        assertEquals(listOf(0.9900000000000002, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - absolute value of x`() {
        val script1 = "-10 \n |x| \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(-10.0, calculator.printX1())
        assertEquals(10.0, calculator.printX())
        assertEquals(listOf(10.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - max`() {
        val script1 = "3 \n B↑ \n 2 \n max \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(2.0, calculator.printX1())
        assertEquals(3.0, calculator.printX())
        assertEquals(listOf(3.0, 3.0, 0.0, 0.0), calculator.printRegisters())

        val script2 = "СX \n B↑ \n B↑ \n B↑ \n С/П"
        calculator.uploadProgram(script2)
        calculator.calculate()

        val script3 = "2 \n B↑ \n 3 \n max \n С/П"
        calculator.uploadProgram(script3)
        calculator.calculate()
        assertEquals(3.0, calculator.printX1())
        assertEquals(3.0, calculator.printX())
        assertEquals(listOf(3.0, 2.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ЗН`() {
        val script1 = "3 \n ЗН \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(3.0, calculator.printX1())
        assertEquals(1.0, calculator.printX())
        assertEquals(listOf(1.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        val script2 = "СX \n С/П"
        calculator.uploadProgram(script2)
        calculator.calculate()

        val script3 = "-3 \n ЗН \n С/П"
        calculator.uploadProgram(script3)
        calculator.calculate()
        assertEquals(-3.0, calculator.printX1())
        assertEquals(-1.0, calculator.printX())
        assertEquals(listOf(-1.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ∧`() {
        val script1 = "13 \n B↑ \n 11 \n ∧ \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(11.0, calculator.printX1())
        assertEquals(9.0, calculator.printX())
        assertEquals(listOf(9.0, 13.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ∨`() {
        val script1 = "13 \n B↑ \n 11 \n ∨ \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(11.0, calculator.printX1())
        assertEquals(15.0, calculator.printX())
        assertEquals(listOf(15.0, 13.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ⊕`() {
        val script1 = "13 \n B↑ \n 11 \n ⊕ \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(11.0, calculator.printX1())
        assertEquals(6.0, calculator.printX())
        assertEquals(listOf(6.0, 13.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ¬`() {
        val script1 = "15 \n ¬ \n С/П"
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(15.0, calculator.printX1())
        assertEquals(-16.0, calculator.printX())
        assertEquals(listOf(-16.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - Вx`() {
        val script = readScript(scriptName = "Вx.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(6.0, calculator.printX1())
        assertEquals(3.0, calculator.printX())
        assertEquals(listOf(3.0, 6.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - HM to deg`() {
        val script = readScript(scriptName = "HM to deg.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(2.3, calculator.printX1())
        assertEquals(2.5, calculator.printX(), 1e-6)
        assertRegistersEqual(listOf(2.5, 0.0, 0.0, 0.0), calculator.printRegisters(), 1e-6)
    }

    @Test
    fun `Scenario - deg to HM`() {
        val script = readScript(scriptName = "deg to HM.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(2.5, calculator.printX1())
        assertEquals(2.3, calculator.printX(), 1e-6)
        assertRegistersEqual(listOf(2.3, 0.0, 0.0, 0.0), calculator.printRegisters(), 1e-6)
    }

    @Test
    fun `Scenario - HMS to deg`() {
        val script = readScript(scriptName = "HMS to deg.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(2.3030, calculator.printX1())
        assertEquals(2.5083333333333333, calculator.printX(), 1e-6)
        assertRegistersEqual(listOf(2.5083333333333333, 0.0, 0.0, 0.0), calculator.printRegisters(), 1e-6)
    }

    @Test
    fun `Scenario - deg to HMS`() {
        val script = readScript(scriptName = "deg to HMS.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(2.5083333333, calculator.printX1())
        assertEquals(2.3030, calculator.printX(), 1e-5)
        assertRegistersEqual(listOf(2.3030, 0.0, 0.0, 0.0), calculator.printRegisters(), 1e-5)
    }

    @Test
    fun `Scenario - СЧ`() {
        val script = readScript(scriptName = "СЧ.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        val result = calculator.printX()
        assertTrue(result >= 0.0 && result < 1.0)
    }

    @Test
    fun `Scenario - X→П`() {
        val script = readScript(scriptName = "X→П.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(12.0, calculator.printX())
        assertEquals(listOf(12.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        assertEquals(15.0, calculator.printDataRegister("8"))
        assertEquals(12.0, calculator.printDataRegister("d"))
    }

    @Test
    fun `Scenario - П→X`() {
        val script1 = readScript(scriptName = "П→X_1.mk61")
        calculator.uploadProgram(script1)
        calculator.calculate()
        assertEquals(15.0, calculator.printX())
        assertEquals(listOf(15.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        val script2 = readScript(scriptName = "П→X_2.mk61")
        calculator.uploadProgram(script2)
        calculator.calculate()
        assertEquals(12.0, calculator.printX())
        assertEquals(listOf(12.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - БП`() {
        val script = readScript(scriptName = "БП.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - IF X less than 0`() {
        val script = readScript(scriptName = "IF X less than 0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(7.0, calculator.printX())
        assertEquals(listOf(7.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - IF X more or same than 0`() {
        val script = readScript(scriptName = "IF X more or same than 0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(1.0, calculator.printX())
        assertEquals(listOf(1.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - IF X is 0`() {
        val script = readScript(scriptName = "IF X is 0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(7.0, calculator.printX())
        assertEquals(listOf(7.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - IF X not 0`() {
        val script = readScript(scriptName = "IF X not 0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(2.0, calculator.printX())
        assertEquals(listOf(2.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - ПП`() {
        var script = readScript(scriptName = "ПП 1.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(8.0, calculator.printDataRegister("8"))
        assertEquals(6.0, calculator.printX1())
        assertEquals(2.0, calculator.printX())
        assertEquals(listOf(2.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "ПП 2.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(8.0, calculator.printDataRegister("8"))
        assertEquals(6.0, calculator.printX1())
        assertEquals(2.0, calculator.printX())
        assertEquals(listOf(2.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "ПП 3.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(8.0, calculator.printDataRegister("5"))
        assertEquals(32.0, calculator.printDataRegister("7"))
        assertEquals(67.0, calculator.printDataRegister("8"))
        assertEquals(35.0, calculator.printDataRegister("9"))
        assertEquals(67.0, calculator.printDataRegister("a"))
        assertEquals(6.0, calculator.printX1())
        assertEquals(61.0, calculator.printX())
        assertEquals(listOf(61.0, 35.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "ПП 4.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(8.0, calculator.printDataRegister("5"))
        assertEquals(32.0, calculator.printDataRegister("7"))
        assertEquals(67.0, calculator.printDataRegister("8"))
        assertEquals(35.0, calculator.printDataRegister("9"))
        assertEquals(67.0, calculator.printDataRegister("a"))
        assertEquals(6.0, calculator.printX1())
        assertEquals(61.0, calculator.printX())
        assertEquals(listOf(61.0, 35.0, 35.0, 35.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - L0`() {
        val script = readScript(scriptName = "L0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(6.0, calculator.printDataRegister("a"))
        assertEquals(10.0, calculator.printDataRegister("9"))
    }

    @Test
    fun `Scenario - L1`() {
        val script = readScript(scriptName = "L1.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(6.0, calculator.printDataRegister("a"))
        assertEquals(10.0, calculator.printDataRegister("9"))
    }

    @Test
    fun `Scenario - L2`() {
        val script = readScript(scriptName = "L2.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(6.0, calculator.printDataRegister("a"))
        assertEquals(10.0, calculator.printDataRegister("9"))
    }

    @Test
    fun `Scenario - L3`() {
        val script = readScript(scriptName = "L3.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(6.0, calculator.printDataRegister("a"))
        assertEquals(10.0, calculator.printDataRegister("9"))
    }

    @Test
    fun `Scenario - K X→П`() {
        val script = readScript(scriptName = "K X→П.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(12.0, calculator.printX())
        assertEquals(listOf(12.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        assertEquals(15.0, calculator.printDataRegister("1"))
        assertEquals(12.0, calculator.printDataRegister("a"))
    }

    @Test
    fun `Scenario - K П→X`() {
        val script = readScript(scriptName = "K П→X.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(12.0, calculator.printX())
        assertEquals(listOf(12.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        assertEquals(14.0, calculator.printDataRegister("8"))
        assertEquals(12.0, calculator.printDataRegister("e"))
    }

    @Test
    fun `Scenario - K БП`() {
        var script = readScript(scriptName = "K БП 0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "K БП 1.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "K БП 2.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "K БП 3.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "K БП 4.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "K БП 5.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "K БП 6.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "K БП 7.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(8.0, calculator.printX())
        assertEquals(listOf(8.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - K IF X less than 0`() {
        val script = readScript(scriptName = "K IF X less than 0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(7.0, calculator.printX())
        assertEquals(listOf(7.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - K IF X more or same than 0`() {
        val script = readScript(scriptName = "K IF X more or same than 0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(1.0, calculator.printX())
        assertEquals(listOf(1.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - K IF X is 0`() {
        val script = readScript(scriptName = "K IF X is 0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(7.0, calculator.printX())
        assertEquals(listOf(7.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - K IF X not 0`() {
        val script = readScript(scriptName = "K IF X not 0.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(0.0, calculator.printDataRegister("8"))
        assertEquals(2.0, calculator.printX1())
        assertEquals(2.0, calculator.printX())
        assertEquals(listOf(2.0, 0.0, 0.0, 0.0), calculator.printRegisters())
    }

    @Test
    fun `Scenario - K ПП`() {
        var script = readScript(scriptName = "K ПП 1.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(8.0, calculator.printDataRegister("8"))
        assertEquals(6.0, calculator.printX1())
        assertEquals(2.0, calculator.printX())
        assertEquals(listOf(2.0, 0.0, 0.0, 0.0), calculator.printRegisters())

        script = readScript(scriptName = "K ПП 2.mk61")
        calculator.uploadProgram(script)
        calculator.calculate()
        assertEquals(8.0, calculator.printDataRegister("5"))
        assertEquals(32.0, calculator.printDataRegister("7"))
        assertEquals(67.0, calculator.printDataRegister("8"))
        assertEquals(35.0, calculator.printDataRegister("9"))
        assertEquals(67.0, calculator.printDataRegister("a"))
        assertEquals(6.0, calculator.printX1())
        assertEquals(61.0, calculator.printX())
        assertEquals(listOf(61.0, 35.0, 0.0, 0.0), calculator.printRegisters())
    }


}
