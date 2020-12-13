package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsSequence


class Day1: Day() {
    val GOAL = 2020
    val numbers = fileAsSequence("day1.txt").map { it.toInt() }.toList()

    override fun part1(): Int {
        // Find the two numbers that sum up to 2020
        val first = numbers.first { numbers.contains(GOAL - it) }

        return first * (GOAL - first)
    }

    override fun part2(): Int {
        for (a in numbers) {
            for(b in numbers) {
                if(a + b >= GOAL)
                    continue

                for(c in numbers) {
                    if(a + b + c == GOAL) {
                        return a * b * c
                    }
                }
            }
        }

        return 0
    }
}