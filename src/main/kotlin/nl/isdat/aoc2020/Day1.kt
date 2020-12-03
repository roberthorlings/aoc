package nl.isdat.aoc2020

import nl.isdat.aoc2020.Utils.fileAsLines

const val GOAL = 2020

object Day1 {
    @JvmStatic
    fun main(args: Array<String>) {
        val numbers = fileAsLines("day1.txt").map { it.toInt() }.toList()

        part1(numbers)
        part2(numbers)
    }

    private fun part1(numbers: List<Int>) {
        // Find the two numbers that sum up to 2020
        val first = numbers.first { numbers.contains(GOAL - it) }

        println("Result: " + (first * (GOAL - first)))
    }

    private fun part2(numbers: List<Int>) {
        outer@ for (a in numbers) {
            for(b in numbers) {
                if(a + b >= GOAL)
                    continue

                for(c in numbers) {
                    if(a + b + c == GOAL) {
                        println("Result: " + (a * b * c))
                        break@outer
                    }
                }
            }
        }
    }

}