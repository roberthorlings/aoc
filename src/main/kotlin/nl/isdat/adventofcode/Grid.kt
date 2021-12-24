package nl.isdat.adventofcode

data class Grid<T>(private val data: List<List<T>>) {
    val rows = data.size
    val cols = data[0].size
    val size = rows * cols

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
     * Returns a map from point to the current value
     */
    fun entries(): Map<Point, T> =
        data.flatMapIndexed { rowIdx, row ->
            row.mapIndexed { colIdx, entry ->
                Point(colIdx, rowIdx) to entry
            }
        }
        .toMap()

    fun <V>map(operator: (T) -> V): Grid<V> =
        fromEntries(
            entries()
                .mapValues { (_, v) -> operator(v) }
        )

    fun <V>map(operator: (Point, T) -> V): Grid<V> =
        fromEntries(
            entries()
                .mapValues { (point, v) -> operator(point, v) }
        )

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

    companion object {
        fun <T> fromEntries(entries: Map<Point, T>): Grid<T>{
            if(entries.isEmpty())
                throw IllegalArgumentException("Empty entries list is not supported")

            val coordinates = entries.keys
            val numRows = coordinates.map { it.y }.maxOrNull()!!
            val numCols = coordinates.map { it.x }.maxOrNull()!!

            val data = (0..numRows).map { row ->
                (0..numCols).map { col ->
                    entries[Point(col, row)] ?: throw IllegalArgumentException("No entry found for point ${col}, ${row}")
                }
            }

            return Grid(data)
        }
    }
}