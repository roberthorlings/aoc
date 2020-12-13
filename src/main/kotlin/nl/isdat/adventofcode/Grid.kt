package nl.isdat.adventofcode

data class Grid<T>(private val data: List<List<T>>) {
    val rows = data.size
    val cols = data[0].size

    operator fun get(p: Point): T = data[p.y][p.x]

    fun neighbours(point: Point): Collection<Point> = point.neighbours().filter {
        it.x >= 0 && it.x < cols && it.y >= 0 && it.y < rows
    }

    fun points(): Sequence<Point> = sequence {
        (0 until rows).forEach { row ->
            (0 until cols).forEach { col ->
                yield(Point(col, row))
            }
        }
    }

    override fun toString(): String {
        return data.map { it.joinToString("") }.joinToString("\n")
    }
}