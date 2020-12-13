package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Grid
import nl.isdat.adventofcode.Input.fileAsList
import nl.isdat.adventofcode.Point


class Day11 : Day() {
    val grid = fileAsList("2020/day11.txt").let { lines -> Grid(lines.map { it.toList() }) }

    override fun part1(): Int {
        val simulation = Simulation(grid)
        var previous = grid
        var next = simulation.simulateGeneration()

        while(next != previous) {
            previous = next
            next = simulation.simulateGeneration()
        }

        return simulation.grid.points().count { simulation.grid[it] == OCCUPPIED }
    }

    class Simulation(var grid: Grid<Char>) {
        fun simulateGeneration(): Grid<Char> {
            grid = Grid(
                (0 until grid.rows).map { row ->
                    (0 until grid.cols).map { col ->
                        simulateAtPoint(Point(col, row))
                    }
                }
            )

            return grid
        }

        private fun simulateAtPoint(point: Point): Char {
            val current = grid[point]
            return when (current) {
                // If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
                EMPTY -> if(grid.neighbours(point).none { grid[it] == OCCUPPIED } ) OCCUPPIED else EMPTY

                // If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
                OCCUPPIED -> if(grid.neighbours(point).count { grid[it] == OCCUPPIED } >= 4 ) EMPTY else OCCUPPIED

                //  Otherwise, the seat's state does not change.
                else -> current
            }
        }
    }

    companion object {
        const val EMPTY = 'L'
        const val OCCUPPIED = '#'
        const val FLOOR = '.'
    }
}