package nl.isdat.adventofcode

import kotlin.system.measureTimeMillis

abstract class Day {
    open fun part1(): Any = "[TODO]"
    open fun part2(): Any = "[TODO]"

    fun run() {
        println("Running ${this.javaClass}")

        val part1Duration = measureTimeMillis { println("Part 1 answer: " + part1()) }
        println("  duration: ${part1Duration}ms")
        val part2Duration = measureTimeMillis { println("Part 2 answer: " + part2()) }
        println("  duration: ${part1Duration}ms")
    }
}