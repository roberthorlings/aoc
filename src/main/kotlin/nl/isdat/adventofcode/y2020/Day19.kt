package nl.isdat.adventofcode.y2020

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsString
import java.lang.IllegalArgumentException

class Day19 : Day() {
    val input = fileAsString("2020/day19.txt")
    val rules = parse(input)
    val messages = input.split("\n\n")[1].lines()

    override fun part1(): Int {
        val matcher = Matcher(rules)
        return messages.count(matcher::matches)
    }
    override fun part2(): Int {
        val matcher = Matcher(
            rules
                + (8 to parseRule("42 | 42 8"))
                + (11 to parseRule("42 31 | 42 11 31"))
        )

        return messages.count(matcher::matches)
    }

    class Matcher(private val rulesMap: Map<Int, Rule>) {
        fun matches(message: String, ruleList: List<Rule> = listOf(rulesMap[0]!!)): Boolean {
            if (message.isEmpty() && ruleList.isEmpty()) return true
            if (message.isEmpty() || ruleList.isEmpty()) return false

            val rule = ruleList[0]
            return when (rule) {
                is Rule.Literal -> message.first() == rule.character && matches(message.drop(1), ruleList.drop(1))
                is Rule.Either -> rule.rules.any { matches(message, listOf(it) + ruleList.drop(1)) }
                is Rule.Concatenation -> matches(message, rule.rules.map { rulesMap[it]!! } + ruleList.drop(1))
            }
        }
    }

    private fun parse(input: String): Map<Int, Rule> {
        val rules = input.split("\n\n")[0]

        return rules.lines().map(::parseRules).toMap()
    }

    private fun parseRules(line: String): Pair<Int, Rule> {
        val (idx, ruleStr) = line.split(":").map(String::trim)
        return idx.toInt() to parseRule(ruleStr)
    }

    private fun parseRule(input: String): Rule {
        if(input.contains("|")) {
            return Rule.Either(input.split("|").map { parseRule(it.trim()) })
        }

        if(CONCAT.matches(input)) {
            return Rule.Concatenation(input.split(" ").map { it.trim().toInt() })
        }

        if(LITERAL.matches(input)) {
            return Rule.Literal(input.trim().first { it != '"' })
        }

        throw IllegalArgumentException("Invalid rule given: ${input}")
    }

    sealed class Rule {
        data class Literal(val character: Char): Rule()
        data class Either(val rules: Collection<Rule>): Rule()
        data class Concatenation(val rules: Collection<Int>): Rule()
    }

    companion object {
        val LITERAL = Regex("^\\s*\"([a-z])\"\\s*$")
        val EITHER = Regex("^(.+) \\| (.+)$")
        val CONCAT = Regex("(\\d+)( (\\d+))*")
    }
}
