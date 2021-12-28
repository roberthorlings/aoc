package nl.isdat.adventofcode.y2021

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input
import nl.isdat.adventofcode.memoize

class Day14(private val fileName: String = "2021/day14.txt"): Day() {
    data class InputData(val template: String, val rules: Map<String, String>)
    private fun String.countChars() = this.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    private val input = parseInput()

    private fun parseInput(): InputData {
        val lines = Input.fileAsList(fileName)

        return InputData(
            template = lines.first(),
            rules = lines
                .drop(2)
                .map {
                    val parts = it.split(" -> ")
                    parts[0] to parts[1]
                }
                .toMap()
        )
    }

    private fun runOptimizedAlgorithm(steps: Int): Long {
        val characterFrequencies = input.template
            .windowed(2)
            .map { numChars(it, steps) }
            .reduce { current, next -> sumFrequencies(current, next) }
            .let { counts ->
                // Because of the windows of 2, all 'middle' characters in the initial template
                // have been counted twice
                val mutableMap = counts.toMutableMap()

                input.template.drop(1).dropLast(1)
                    .countChars()
                    .forEach { (c, count) ->
                        mutableMap[c] = mutableMap[c]!! - count
                    }

                mutableMap
            }

        if (characterFrequencies.isEmpty()) return 0

        return (characterFrequencies.values.maxOrNull()!! - characterFrequencies.values.minOrNull()!!).toLong()
    }

    private val memoizedValues: MutableMap<Pair<String, Int>, Map<Char, Long>> = mutableMapOf()

    private fun numChars(template: String, depth: Int): Map<Char, Long> {
        return memoizedValues.getOrPut(template to depth) {
            if(depth == 0) {
                template.countChars()
            } else {
                val insert = input.rules[template]
                if (insert == null) {
                    template.countChars()
                } else {
                    sumFrequencies(
                        numChars("${template[0]}${insert}", depth - 1),
                        numChars("${insert}${template[1]}", depth - 1),
                        mapOf(insert[0] to -1)
                    )
                }
            }
        }
    }

    private fun sumFrequencies(vararg maps: Map<Char, Long>): Map<Char, Long> =
        maps.reduce { current, nextMap ->
            nextMap.entries.fold(current) { freq, entry ->
                val currentCount = freq[entry.key] ?: 0
                freq + (entry.key to entry.value + currentCount)
            }
        }

    override fun part1(): Long {
        return runOptimizedAlgorithm(10)
    }

    override fun part2(): Long {
        return runOptimizedAlgorithm(40)
    }

}