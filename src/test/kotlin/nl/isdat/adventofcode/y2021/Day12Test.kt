package nl.isdat.adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Test {
    @Test
    fun `should handle test input correctly for part 1`() {
        assertEquals(10, Day12("2021/day12-1.test.txt").part1())
        assertEquals(19, Day12("2021/day12-2.test.txt").part1())
        assertEquals(226, Day12("2021/day12-3.test.txt").part1())
    }

    @Test
    fun `should handle test input correctly for part 2`() {
        assertEquals(36, Day12("2021/day12-1.test.txt").part2())
        assertEquals(103, Day12("2021/day12-2.test.txt").part2())
        assertEquals(3509, Day12("2021/day12-3.test.txt").part2())
    }
}