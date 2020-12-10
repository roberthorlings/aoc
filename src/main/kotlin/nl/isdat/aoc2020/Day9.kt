package nl.isdat.aoc2020

import nl.isdat.aoc2020.Utils.fileAsSequence

object Day9 {
    @JvmStatic
    fun main(args: Array<String>) {
        val values = fileAsSequence("day9.txt").map(String::toLong).toList()

        part1(values)
        part2(values)
    }

    private fun part1(values: List<Long>) {
        val xmasPort = XmasPort(25, values)

        println("Part 1 result: " + xmasPort.invalidNumbers.first())
    }

    private fun part2(values: List<Long>) {
        val result: Long = 217430975

        // Only considers numbers up to this value, as numbers above will be larger
        val eligible = values.takeWhile { it != result }

        val possibleWindowSizes = 3 until eligible.size

        for(windowSize in possibleWindowSizes) {
            val window = eligible.windowed(windowSize).firstOrNull { it.sum() == result }

            if(window != null) {
                // A match was found
                println("Part 2 result: " + (window.minOrNull()!! + window.maxOrNull()!!))
            }
        }


    }


    class XmasPort(val windowSize: Int, val values: List<Long>) {
        val invalidNumbers = values
            .windowed(windowSize + 1)
            .filter { window -> !isValid(window.dropLast(1), window.last()) }
            .map { it.last() }

        /**
         * A value is valid if it is the sum of two numbers in the given list
         */
        private fun isValid(previous: List<Long>, value: Long): Boolean {
            val eligible = previous.filter { it <= Math.ceil(value / 2.0) }

            return eligible.any { a -> previous.any { b -> a + b == value } }
        }

    }
}