package nl.isdat.adventofcode

data class Point(val x: Int, val y: Int) {
    fun neighbours() = NEIGHBOUR_VECTORS.map { this + it }

    operator fun times(count: Int) = Point(count * x, count * y)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    fun rotateClockwise() = Point(x = y, y = -x)
    fun rotateCounterClockwise() = Point(x = -y, y = x)

    /**
     * Rturns the manhattan distance between this point and the other
     */
    fun manhattan(other: Point): Int = Math.abs(x - other.x) + Math.abs(y - other.y)

    companion object {
        val NEIGHBOUR_VECTORS = listOf(-1, 0, 1).flatMap { x -> listOf(-1, 0, 1).map { y -> Point(x, y) } }.filter { it != Point(0, 0) }
    }
}
