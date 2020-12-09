package nl.isdat.aoc2020

import nl.isdat.aoc2020.Utils.fileAsSequence

object Day2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val combinations = fileAsSequence("day2.txt").map(::parseLine).toList()

        part1(combinations)
        part2(combinations)
    }

    private fun part1(combinations: List<PasswordWithRule>) {
        val numValid = combinations.count { it.rule.validAccordingToOldRule(it.password) }
        println("Part 1 result: " + numValid)
    }

    private fun part2(combinations: List<PasswordWithRule>) {
        val numValid = combinations.count { it.rule.validAccordingToNewRule(it.password) }
        println("Part 2 result: " + numValid)
    }

    fun parseLine(line: String) =
        line.split(" ")
            .let { parts ->
                val minAndMax = parts[0].split("-").map { it.toInt() }
                val character = parts[1][0]
                val password = parts[2].trim()

                PasswordWithRule(
                    PasswordRule(a = minAndMax[0], b = minAndMax[1], character = character),
                    password
                )
            }

    data class PasswordRule(val a: Int, val b: Int, val character: Char) {
        fun validAccordingToOldRule(password: String) = password.count { it == character } in (a..b)
        fun validAccordingToNewRule(password: String) = (password[a - 1] == character) xor (password[b - 1] == character)
    }

    data class PasswordWithRule(val rule: PasswordRule, val password: String)

}