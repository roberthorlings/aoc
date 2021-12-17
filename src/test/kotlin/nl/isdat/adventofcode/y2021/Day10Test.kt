package nl.isdat.adventofcode.y2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun testParseLine() {
        assertEquals(Day10.Companion.LineStatus.GOOD, Day10.parseLine("()"))
        assertEquals(Day10.Companion.LineStatus.GOOD, Day10.parseLine("{()()()}"))
        assertEquals(Day10.Companion.LineStatus.GOOD, Day10.parseLine("[<>({}){}[([])<>]]"))
        assertEquals(Day10.Companion.LineStatus.GOOD, Day10.parseLine("(((((((((())))))))))"))

        assertTrue(Day10.parseLine("(]") is Day10.Companion.LineStatus.CORRUPT)
        assertTrue(Day10.parseLine("<([]){()}[{}])") is Day10.Companion.LineStatus.CORRUPT)
        assertTrue(Day10.parseLine("(((()))}") is Day10.Companion.LineStatus.CORRUPT)

        assertTrue(Day10.parseLine("((") is Day10.Companion.LineStatus.INCOMPLETE)
        assertTrue(Day10.parseLine("(({}[])") is Day10.Companion.LineStatus.INCOMPLETE)
    }

    @Test
    fun `should handle test input correctly for part 1`() {
        assertEquals(26397, Day10("2021/day10.test.txt").part1())
    }

    @Test
    fun `should handle test input correctly for part 2`() {
        assertEquals(288957, Day10("2021/day10.test.txt").part2())
    }

}