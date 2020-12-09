package nl.isdat.aoc2020

import nl.isdat.aoc2020.Utils.fileAsSequence

object Day3 {
    @JvmStatic
    fun main(args: Array<String>) {
        val rows = fileAsSequence("day3.txt").map { Row(it) }.toList()

        part1(rows)
        part2(rows)
    }

    private fun part1(rows: List<Row>) {
        val numTrees = countTreesForSlope(rows, Slope(3, 1))

        println("Part 1 result: " + numTrees)
    }

    private fun part2(rows: List<Row>) {
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

        println("Part 1 result: " + total)
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