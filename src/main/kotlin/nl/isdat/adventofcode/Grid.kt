package nl.isdat.adventofcode

data class Grid<T>(private val data: List<List<T>>) {
    val rows = data.size
    val cols = data[0].size

    operator fun get(p: Point): T = data[p.y][p.x]

    /**
     * Returns true if this point is present in this grid
     */
    fun contains(point: Point): Boolean = point.x >= 0 && point.x < cols && point.y >= 0 && point.y < rows

    /**
     * Returns a collection of points which are the neighbours (horizontal, vertical and diagonal) of the given point
     */
    fun neighbours(point: Point): Collection<Point> = point.neighbours().filter(this::contains)

    /**
     * Returns a collection of points which are the direct neighbours (horizontal, vertical but not diagonal) of the given point
     */
    fun directNeighbours(point: Point): Collection<Point> = point.directNeighbours().filter(this::contains)

    /**
     * Returns a sequence of all points within this grid
     */
    fun points(): Sequence<Point> = sequence {
        (0 until rows).forEach { row ->
            (0 until cols).forEach { col ->
                yield(Point(col, row))
            }
        }
    }

    /**
     * Returns a sequence of the grid items that are found when starting at the given point and going into the given direction
     */
    fun rayInDirection(point: Point, direction: Point): Sequence<T> = sequence {
        var current = point + direction
        while(contains(current)) {
            yield(get(current))
            current += direction
        }
    }

    override fun toString(): String {
        return data.map { it.joinToString("") }.joinToString("\n")
    }
}