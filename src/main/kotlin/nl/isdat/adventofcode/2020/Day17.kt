package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsList
import nl.isdat.adventofcode.Vector

class Day17 : Day() {
    val activePoints = fileAsList("2020/day17.txt").let(::parse)

    override fun part1(): Int = (1..6).fold(
        activePoints.map { Vector(it.values + 0 )},
        { points, i -> iterate(points) }
    ).size

    override fun part2(): Int = (1..6).fold(
        activePoints.map { Vector(it.values + listOf(0, 0))},
        { points, i ->
            println("Iteration ${i} - # points: ${points.size}")
            iterate(points)
        }
    ).size

    fun iterate(activePoints: Collection<Vector>): List<Vector> {
        // Each iteration, we only have to consider the neighbours of the currently
        // active points, points without active neighbours do not change state
        val eligible = (activePoints.flatMap { it.neighbours() } + activePoints).distinct()
        fun isActive(point: Vector) = activePoints.contains(point)

        return eligible.filter { point ->
            val activeNeighbours = point.neighbours().count(::isActive)
            if(isActive(point)) {
                // This point will remain active as long as it has 2 or 3 active neighbours
                (activeNeighbours == 2 || activeNeighbours == 3)
            } else {
                // This point will become active if it has 3 active neighbours
                activeNeighbours == 3
            }
        }
    }

    private fun parse(lines: List<String>): Collection<Vector> =
        lines.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                if(c == '#')
                    Vector(x, y)
                else
                    null
            }
        }
}