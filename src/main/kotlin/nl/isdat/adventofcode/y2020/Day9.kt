package nl.isdat.adventofcode.y2020

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsSequence

class Day9: Day() {
    val values = fileAsSequence("2020/day9.txt").map(String::toLong).toList()

    override fun part1(): Long = XmasPort(25, values).invalidNumbers.first()

    override fun part2(): Long {
        val result: Long = 217430975

        // Only considers numbers up to this value, as numbers above will be larger
        val eligible = values.takeWhile { it != result }

        val possibleWindowSizes = 3 until eligible.size

        for(windowSize in possibleWindowSizes) {
            val window = eligible.windowed(windowSize).firstOrNull { it.sum() == result }

            if(window != null) {
                // A match was found
                return window.minOrNull()!! + window.maxOrNull()!!
            }
        }

        System.err.println("No series found that matches the required sum")
        return 0
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