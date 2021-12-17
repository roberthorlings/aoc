package nl.isdat.adventofcode.y2021

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day9Test {
    @Test
    fun `should handle test input correctly for part 1`() {
        assertEquals(Day9("2021/day9.test.txt").part1(), 15)
    }

    @Test
    fun `should handle test input correctly for part 2`() {
        assertEquals(Day9("2021/day9.test.txt").part2(), 1134)
    }

}