package nl.isdat.adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun `should handle simple test input correctly for part 1`() {
        assertEquals(9, Day11("2021/day11-1.test.txt", 2).part1())
    }


    @Test
    fun `should handle test input correctly for part 1`() {
        assertEquals(204, Day11("2021/day11-2.test.txt", 10).part1())
        assertEquals(1656, Day11("2021/day11-2.test.txt", 100).part1())
    }

    @Test
    fun `should handle test input correctly for part 2`() {
        assertEquals(195, Day11("2021/day11-2.test.txt").part2())
    }

}