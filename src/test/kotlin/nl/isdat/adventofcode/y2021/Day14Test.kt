package nl.isdat.adventofcode.y2021

import nl.isdat.adventofcode.y2020.Day14
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14Test {
    @Test
    fun `should handle test input correctly for part 1`() {
        assertEquals(1588, Day14("2021/day14.test.txt").part1())
    }

    @Test
    fun `should handle test input correctly for part 2`() {
        assertEquals(2188189693529, Day14("2021/day14.test.txt").part2())
    }
}