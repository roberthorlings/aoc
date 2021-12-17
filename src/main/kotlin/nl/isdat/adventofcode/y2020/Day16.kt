package nl.isdat.adventofcode.y2020

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsList

class Day16 : Day() {
    val data = fileAsList("2020/day16.txt").let(::parse)

    override fun part1(): Int = data.nearby.flatMap { it.invalidValues(data.rules) }.sum()

    override fun part2(): Long {
        // Filter out invalid tickets
        val validTickets = data.nearby.filter { it.isValid(data.rules) }

        // Put all values for a given field in a bin, with the field index as key
        val groupedValues = validTickets
            .flatMap { it.values.mapIndexed { index, i -> index to i } }
            .groupBy({it.first}, {it.second})

        // Find possible fields for each bin
        val possibleFields = groupedValues
            .map { (idx, values) ->
                // Find all rules for which all values match
                IndexedValue(idx, data.rules.filter { rule -> values.all(rule::isValid) })
            }
            .sortedBy { it.value.size }

        // Find the right assignment
        val rulesMap = assignRules(0, emptyList(), possibleFields) ?: throw IllegalStateException("No solution found")

        // Multiply the appropriate values
        return rulesMap
            .filter { (idx, rule) -> rule.name.startsWith("departure") }
            .map { (idx, rule) -> data.own.values[idx].toLong() }
            .reduce { a, b -> a * b }
    }

    private fun assignRules(i: Int, used: List<Rule>, possibleFields: List<IndexedValue<List<Rule>>>): Map<Int, Rule>? {
        if(i >= possibleFields.size)
            return emptyMap()

        val index = possibleFields[i].index
        val options = possibleFields[i].value - used

        // If no rules are left for this field, return immediately
        if(options.isEmpty())
            return null

        for(rule in options) {
            // Check the rest of the list for possible assignments
            val nested = assignRules(i + 1, used + rule, possibleFields)

            // If the rest can be assigned, add this rule to the map and return.
            if (nested != null) {
                return nested + (index to rule)
            }

            // Otherwise, try the next rule
        }

        // If none of the rules have a valid assignment for the other fields, return null as well
        return null
    }

    private fun parse(lines: List<String>): Data {
        val ruleLines = lines.takeWhile { it != "" }
        val ownTicketLine = lines[ruleLines.size + 2]
        val nearbyTicketsLines = lines.drop(ruleLines.size + 4).dropWhile { !it[0].isDigit() }

        return Data(
            rules = ruleLines.map(::parseRule).toList(),
            own = Ticket(ownTicketLine),
            nearby = nearbyTicketsLines.map { Ticket(it) }.toList()
        )
    }

    private fun parseRule(s: String): Rule {
        val (name, ranges) = s.split(":")

        return Rule(
            name,
            ranges.split(" or ").map { it.split("-").map(String::trim).let { values -> values[0].toInt()..values[1].toInt() }}
        )
    }


    data class Ticket(val values: List<Int>) {
        constructor(valueString: String): this(valueString.split(",").map(String::toInt))

        fun isValid(rules: List<Rule>) = invalidValues(rules).isEmpty()
        fun invalidValues(rules: List<Rule>) = values.filter { value -> rules.none { it.isValid(value) } }
    }
    data class Rule(val name: String, val validRanges: List<IntRange>) {
        fun isValid(value: Int) = validRanges.any { it.contains(value) }
    }

    data class Data(
        val rules: List<Rule>,
        val own: Ticket,
        val nearby: List<Ticket>
    )

}