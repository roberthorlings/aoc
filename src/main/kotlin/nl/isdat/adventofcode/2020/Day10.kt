package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsSequence

class Day10: Day() {
    val adapters = fileAsSequence("day10.txt").map(String::toLong).toList().sorted()
    val deviceAdapter = adapters.last() + 3

    override fun part1(): Int {
        // Add the outlet and the device adapter
        val allAdapters = listOf(0L) + adapters + deviceAdapter

        // Check differences between adjacent adapters
        val differences = allAdapters.windowed(2).map { it[1] - it[0] }

        // Count all differences
        val counts = differences.groupingBy { it }.eachCount()

        return counts[1]!! * counts[3]!!
    }

    override fun part2(): Long {
        // Add the device adapter
        val allAdapters = adapters + deviceAdapter

        return configurationsEligible(0L, allAdapters)
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