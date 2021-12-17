package nl.isdat.adventofcode.y2021

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Grid
import nl.isdat.adventofcode.Input
import nl.isdat.adventofcode.Point

class Day9(private val fileName: String = "2021/day9.txt"): Day() {
    val grid = Input.fileAsList(fileName).let { lines -> Grid(lines.map { it.toList().map(Character::getNumericValue) }) }

    override fun part1(): Long = findLowPoints()
        .map{ lowPoint -> grid[lowPoint] + 1 }
        .sum()
        .toLong()

    override fun part2(): Long = findLowPoints()
        .map(::findBassin)
        .map { it.size.toLong() }
        .sortedDescending()
        .take(3)
        .reduce { a, b -> a * b }

    private fun findBassin(initial: Point): Set<Point> {
        val bassin = mutableSetOf<Point>(initial)
        val openPoints = mutableSetOf<Point>(initial)

        while (openPoints.isNotEmpty()) {
            val current = openPoints.first()
            val neighbours = findHigherNeighbours(current)

            // Only handle neighbours we have not seen yet
            val newNeighbours = neighbours.subtract(bassin)

            bassin += newNeighbours
            openPoints += newNeighbours
            openPoints -= current
        }

        return bassin
    }

    private fun findHigherNeighbours(point: Point) =
        grid.directNeighbours(point)
            .filter { neighbour -> grid[neighbour] < 9 }
            .filter { neighbour -> grid[neighbour] >= grid[point] }

    private fun findLowPoints() = grid.points()
            .filter { point ->
                val currentValue = grid[point]
                grid
                    .directNeighbours(point)
                    .map { neighbour -> grid.get(neighbour) }
                    .all { it >= currentValue }
            }
}