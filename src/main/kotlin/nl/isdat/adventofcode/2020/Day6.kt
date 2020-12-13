package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsSequence

class Day6: Day() {
    val groups = parse(fileAsSequence("day6.txt")).toList()

    override fun part1(): Int = groups.sumBy { it.uniqueQuestionsAnswered.size }
    override fun part2(): Int = groups.sumBy { it.questionsAnsweredByEveryone.size }

    private fun parse(input: Sequence<String>): Sequence<Group> = sequence {
        var list = input.toList()

        while (list.isNotEmpty()) {
            println(list.size)
            val group = list
                .takeWhile { it.isNotEmpty() }
                .let { Group(it) }

            list = list.drop(group.users.size + 1)

            yield(group)
        }
    }

    data class Group(val users: List<String>) {
        val uniqueQuestionsAnswered = users.joinToString("").toList().distinct()
        val questionsAnsweredByEveryone = uniqueQuestionsAnswered.filter { question -> users.all { user -> user.contains(question) } }.toList()
    }

}