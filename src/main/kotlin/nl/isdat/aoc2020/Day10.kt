package nl.isdat.aoc2020

import nl.isdat.aoc2020.Utils.fileAsSequence

object Day10 {
    @JvmStatic
    fun main(args: Array<String>) {
        val adapters = fileAsSequence("day10.txt").map(String::toLong).toList().sorted()

        part1(adapters)
        part2(adapters)
    }

    private fun part1(adapters: List<Long>) {
        // Add the outlet and the device adapter
        val allAdapters = listOf(0L) + adapters + (adapters.last() + 3)

        // Check differences between adjacent adapters
        val differences = allAdapters.windowed(2).map { it[1] - it[0] }

        // Count all differences
        val counts = differences.groupingBy { it }.eachCount()

        println("Part 1 result: " + (counts[1]!! * counts[3]!!))
    }

    private fun part2(adapters: List<Long>) {
        // Add the device adapter
        val allAdapters = adapters + (adapters.last() + 3)

        val answer = configurationsEligible(0L, allAdapters)

        println("Part 2 result: " + answer)
    }

    private fun adaptersEligible(current: Long, adapters: List<Long>) =
        adapters.filter { it > current && it <= current + 3 }

    private fun configurationsEligible(current: Long, adapters: List<Long>): Long {
        if (!eligibleAdaptersCache.containsKey(current)) {
            // Return 1 if we reached the last adapter
            val numConfigurations =
                if (current == adapters.last())
                    1L
                else
                    adaptersEligible(current, adapters).map { configurationsEligible(it, adapters) }.sum()

            eligibleAdaptersCache[current] = numConfigurations
        }

        return eligibleAdaptersCache[current]!!
    }

    val eligibleAdaptersCache: MutableMap<Long, Long> = mutableMapOf()
}