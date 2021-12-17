package nl.isdat.adventofcode.y2021

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input

val chunks = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

class Day10(private val fileName: String = "2021/day10.txt"): Day() {
    val navigationOutput = Input.fileAsList(fileName)

    override fun part1(): Long {
        val illegalCharScores = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )

        return navigationOutput
            .map(Day10::parseLine)
            .filter { it is LineStatus.CORRUPT }
            .map { (it as LineStatus.CORRUPT).firstIllegalCharacter }
            .map { illegalCharScores[it] ?: 0 }
            .sum()
            .toLong()
    }

    override fun part2(): Long {
        val missingCharScores = mapOf(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4
        )

        fun score(missing: List<Char>): Long =
            missing
                .fold(0L) { acc, char ->
                    acc * 5 + (missingCharScores[char] ?: 0)
                }

        val scores = navigationOutput
            .map(Day10::parseLine)
            .filter { it is LineStatus.INCOMPLETE }
            .map { score((it as LineStatus.INCOMPLETE).missing) }
            .sorted()

        return scores[Math.floor(scores.size.toDouble() / 2).toInt()]
    }

    companion object {
        fun Char.isOpeningCharacter(): Boolean {
            return chunks.any { it.key == this }
        }
        fun Char.isClosingCharacter(): Boolean {
            return chunks.any { it.value == this }
        }

        sealed class LineStatus {
            object GOOD: LineStatus()
            class CORRUPT(val firstIllegalCharacter: Char): LineStatus()
            class INCOMPLETE(val missing: List<Char>): LineStatus()
        }

        fun parseLine(line: String): LineStatus {
            val expectedCharacters = mutableListOf<Char>()

            for((idx, current) in line.withIndex()) {
                if(current.isOpeningCharacter()) {
                    expectedCharacters.add(chunks[current]!!)
                } else if(expectedCharacters.isNotEmpty() && current == expectedCharacters.last()) {
                    expectedCharacters.removeLast()
                } else {
                    // Unexpected closing character. Line is corrupt
                    return LineStatus.CORRUPT(current)
                }
            }

            return if(expectedCharacters.size > 0)
                LineStatus.INCOMPLETE(expectedCharacters.reversed())
            else
                LineStatus.GOOD
        }
    }
}