package nl.isdat.aoc2020

import nl.isdat.aoc2020.Utils.fileAsLines

object Day6 {
    @JvmStatic
    fun main(args: Array<String>) {
        val groups = parse(fileAsLines("day6.txt")).toList()

        part1(groups)
        part2(groups)
    }

    private fun part1(groups: List<Group>) {
        val answer = groups.sumBy { it.uniqueQuestionsAnswered.size }
        println("Part 1 result: " + answer)
    }

    private fun part2(groups: List<Group>) {
        val answer = groups.sumBy { it.questionsAnsweredByEveryone.size }
        println("Part 2 result: " + answer)
    }

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