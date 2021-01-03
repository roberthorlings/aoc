package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsString

class Day22 : Day() {
    val startingDecks = parse(fileAsString("2020/day22.txt"))

    override fun part1(): Int {
        var decks = startingDecks
        while(!isFinished(decks)) {
            decks = simulateCombat(decks)
        }

        return calculateScore(decks)
    }

    override fun part2(): Int {
        val result = playRecursiveCombat(startingDecks)

        return calculateScore(result.second)
    }

    fun calculateScore(decks: List<List<Int>>): Int {
        val cards = decks.filter { it.isNotEmpty() }.first()
        return cards.zip(cards.size downTo 0).map { it.first * it.second }.sum()
    }

    fun isFinished(decks: List<List<Int>>) = decks.any { it.isEmpty() }
    fun simulateCombat(decks: List<List<Int>>): List<List<Int>> {
        val played = decks.map { it[0] }
        val winner = played.withIndex().maxByOrNull { it.value }!!.index
        return decks.mapIndexed { idx, cards ->
            if(idx == winner) {
                cards.drop(1) + played[winner] + played[1 - winner]
            } else {
                cards.drop(1)
            }
        }
    }

    fun playRecursiveCombat(starting: List<List<Int>>): Pair<Int, List<List<Int>>> {
        var configurationsSeen = mutableSetOf<List<List<Int>>>()
        var decks = starting
        var round = 1
        while(!isFinished(decks)) {
            if(configurationsSeen.contains(decks)) return 0 to decks
            configurationsSeen.add(decks)

            decks = simulateRecursiveCombat(decks)
        }

        return decks.indexOfFirst { it.isNotEmpty() } to decks
    }

    fun simulateRecursiveCombat(decks: List<List<Int>>): List<List<Int>> {
        val played = decks.map { it.first() }
        val winner = if(decks.all { it.first() < it.size }) {
            playRecursiveCombat(
                listOf(
                    decks[0].drop(1).take(played[0]),
                    decks[1].drop(1).take(played[1])
                )
            ).first
        } else {
            played.withIndex().maxByOrNull { it.value }!!.index
        }
        return decks.mapIndexed { idx, cards ->
            if(idx == winner) {
                cards.drop(1) + played[winner] + played[1 - winner]
            } else {
                cards.drop(1)
            }
        }
    }

    private fun parse(input: String): List<List<Int>> =
        input.split("\n\n")
            .map { it.lines().map(String::toInt) }
}
