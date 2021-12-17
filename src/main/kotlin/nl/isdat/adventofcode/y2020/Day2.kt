package nl.isdat.adventofcode.y2020

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsSequence

class Day2: Day() {
    val combinations = fileAsSequence("2020/day2.txt").map(::parseLine).toList()

    override fun part1(): Int =
        combinations.count { it.rule.validAccordingToOldRule(it.password) }

    override fun part2(): Int =
        combinations.count { it.rule.validAccordingToNewRule(it.password) }

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