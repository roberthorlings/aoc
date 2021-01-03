package nl.isdat.adventofcode

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class MatrixTest {
    val matrix = Matrix(
        listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9),
            listOf(10, 11, 12)
        )
    )

    @Test
    internal fun `edges should be returned correctly`() {
        assertEquals(listOf(1, 2, 3), matrix.edges[Direction.NORTH])
        assertEquals(listOf(10, 11, 12), matrix.edges[Direction.SOUTH])
        assertEquals(listOf(1, 4, 7, 10), matrix.edges[Direction.WEST])
        assertEquals(listOf(3, 6, 9, 12), matrix.edges[Direction.EAST])
    }

    @Test
    internal fun `rotate left should be done correctly`() {
        val expected = Matrix(
            listOf(
                listOf(3, 6, 9, 12),
                listOf(2, 5, 8, 11),
                listOf(1, 4, 7, 10)
            )
        )

        assertEquals(expected, matrix.rotateLeft())
        assertEquals(matrix, matrix.rotateLeft().rotateLeft().rotateLeft().rotateLeft())
    }

    @Test
    internal fun `rotate right should be done correctly`() {
        val expected = Matrix(
            listOf(
                listOf(10, 7, 4, 1),
                listOf(11, 8, 5, 2),
                listOf(12, 9, 6, 3)
            )
        )

        assertEquals(expected, matrix.rotateRight())
        assertEquals(matrix, matrix.rotateRight().rotateRight().rotateRight().rotateRight())
    }

    @Test
    internal fun `flip horizontally`() {
        val expected = Matrix(
            listOf(
                listOf(3, 2, 1),
                listOf(6, 5, 4),
                listOf(9, 8, 7),
                listOf(12, 11, 10)
            )
        )

        assertEquals(expected, matrix.flipHorizontally())
    }

    @Test
    internal fun `flip vertically`() {
        val expected = Matrix(
            listOf(
                listOf(10, 11, 12),
                listOf(7, 8, 9),
                listOf(4, 5, 6),
                listOf(1, 2, 3)
            )
        )

        assertEquals(expected, matrix.flipVertically())
    }

    @Test
    internal fun `empty matrix`() {
        assertThrows(IllegalArgumentException::class.java, {
            Matrix<Char>(emptyList())
        })
    }

    @Test
    internal fun `non rectangular matrix`() {
        assertThrows(IllegalArgumentException::class.java, {
            Matrix(listOf(
                listOf(1),
                listOf(1,2)
            ))
        })
    }

}