package nl.isdat.adventofcode

import java.lang.IllegalArgumentException

open class Matrix<T>(val contents: List<List<T>>) {
    init {
        if(contents.isEmpty()) {
            throw IllegalArgumentException("Matrix contents must not be empty")
        }

        if(contents.distinctBy { it.size }.size > 1) {
            throw IllegalArgumentException("A matrix must be rectangular, i.e. all rows must have the same number of elements")
        }

    }

    val edges: Map<Direction, List<T>>
        get() = mapOf(
            Direction.NORTH to contents.first(),
            Direction.SOUTH to contents.last(),
            Direction.WEST to contents.map { it.first() },
            Direction.EAST to contents.map { it.last() }
        )

    fun rotateLeft() = Matrix(
        (contents[0].size - 1 downTo 0)
            .map { idx -> contents.map { it[idx] } }
    )

    fun rotateRight() = Matrix(
        (0 until contents[0].size)
            .map { idx -> contents.reversed().map { it[idx] } }
    )

    fun flipHorizontally() = Matrix(
        contents.map { it.reversed() }
    )

    fun flipVertically() = Matrix(
        contents.reversed()
    )

    override fun equals(other: Any?): Boolean = other is Matrix<*> && other.contents == contents
    override fun hashCode(): Int = 31 * contents.hashCode()
    override fun toString(): String = "Matrix(${contents})"
}