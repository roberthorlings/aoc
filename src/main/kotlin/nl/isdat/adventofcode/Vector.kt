package nl.isdat.adventofcode

import kotlin.math.abs

open class Vector(val values: List<Int>) {
    constructor(vararg args: Int): this(args.asList())

    /**
     * Returns all neighbour points
     */
    open fun neighbours() = neighbourVectors(values.size).map { this + it }

    operator fun get(idx: Int) = values[idx]

    operator fun times(count: Int) = Vector(values.map { it * count })
    operator fun plus(other: Vector) = Vector(values.zip(other.values) { a, b -> a + b })
    operator fun minus(other: Vector) = Vector(values.zip(other.values) { a, b -> a - b })

    /**
     * Returns the manhattan distance between this point and the other
     */
    fun manhattan(other: Vector): Int = values.zip(other.values) { a, b -> abs(a - b) }.sum()

    override fun equals(other: Any?): Boolean = other is Vector && values == other.values
    override fun hashCode(): Int = 31 * values.hashCode()
    override fun toString(): String = "Vector(${values.joinToString(",")})"

    companion object {
        val NEIGHBOURS = listOf(-1, 0, 1)
        val neighbourVectors = { dim: Int ->
            (1..dim)
                .fold(listOf(emptyList<Int>())) { vectors, _ -> vectors.flatMap { v -> NEIGHBOURS.map { v + it } } }
                .filterNot { v -> v.all { it == 0 } }
                .map(::Vector)
        }.memoize()
    }
}
