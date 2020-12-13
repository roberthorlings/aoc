package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsSequence

class Day3: Day() {
    val rows = fileAsSequence("2020/day3.txt").map { Row(it) }.toList()

    override fun part1(): Int =countTreesForSlope(rows, Slope(3, 1))

    override fun part2(): Int {
        val slopes = listOf(
            Slope(1, 1),
            Slope(3, 1),
            Slope(5, 1),
            Slope(7, 1),
            Slope(1, 2)
        )

        val total = slopes.map {
            val count = countTreesForSlope(rows, it)
            println("  Slope ${it} has ${count} trees")
            count
        }.reduce { acc, i -> acc * i }

        return total
    }

    private fun countTreesForSlope(rows: List<Row>, slope: Slope): Int =
        rows
            // Remove rows that would be skipped because of the vertical component of the slope
            .filterIndexed { index, _ -> index % slope.vertical == 0 }

            // For each remaining line, check the correct column
            .filterIndexed { index, row ->
                // The first row is not included in the search
                if (index == 0) {
                    false
                } else {
                    row.hasTree(index * slope.horizontal)
                }
            }
            .count()

    data class Slope(val horizontal: Int, val vertical: Int)
    data class Row(val terrain: String) {
        fun hasTree(idx: Int) = terrain[idx % terrain.length] == '#'
    }

}