package nl.isdat.adventofcode.y2021

import nl.isdat.adventofcode.*

class Day15(private val fileName: String = "2021/day15.txt"): Day() {
    val initialGrid = Input.fileAsList(fileName).let { lines ->
        Grid(lines.map {
            it.toList().map { Character.getNumericValue(it) }
        })
    }

    private fun aStar(grid: Grid<Int>): Long {
        val pathFinding = grid.toPathFinding { _, b -> grid[b] }
        val start = grid.topLeft()
        val goal = grid.bottomRight()

        return pathFinding.aStar(
            start,
            goal,
            { current -> goal.manhattan(current) }
        )
            .drop(1) // Don't count the risk for the start node
            .map { grid[it] }
            .sum()
            .toLong()
    }

    override fun part1(): Long {
        return aStar(initialGrid)
    }

    override fun part2(): Long {
        // Generate enlarged grid
        val nextGrid = { grid: Grid<Int>, add: Int ->
            grid.map { i ->
                if(i + add <= 9)
                    i + add
                else
                    i + add - 9
            }
        }

        val topRow = (1..4).fold(initialGrid) { grid, i ->
            Grid(
                grid.data
                    .zip(
                        nextGrid(initialGrid, i).data
                    ) { a, b -> a + b }
            )
        }

        val enlargedGrid = (1..4).fold(topRow) { grid, i ->
            Grid(
                grid.data + nextGrid(topRow, i).data
            )
        }

        return aStar(enlargedGrid)
    }

}