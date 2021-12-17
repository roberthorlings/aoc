package nl.isdat.adventofcode

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PointTest {
    @Test
    fun `should return correct neighbours`() {
        val expected = setOf(
            Point(4,4), Point(4,5), Point(4,6),
            Point(5,4), Point(5,6),
            Point(6,4), Point(6,5), Point(6,6)
        )
        assertEquals(expected, Point(5,5).neighbours().toSet())
    }

    @Test
    fun `should return correct direct neighbours`() {
        val expected = setOf(
            Point(4,5),
            Point(5,4), Point(5,6),
            Point(6,5)
        )
        assertEquals(expected, Point(5,5).directNeighbours().toSet())
    }
}