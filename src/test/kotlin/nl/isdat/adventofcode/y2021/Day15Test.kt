package nl.isdat.adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day15Test {
    @Test
    fun `should handle test input correctly for part 1`() {
        assertEquals(40, Day15("2021/day15.test.txt").part1())
    }

    @Test
    fun `should handle test input correctly for part 2`() {
        assertEquals(315, Day15("2021/day15.test.txt").part2())
    }
}