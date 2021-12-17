package nl.isdat.adventofcode.y2020

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsString


class Day15 : Day() {
    val numbers = fileAsString("2020/day15.txt").split(",").map(String::toInt).toList()

    val game = sequence {
        // The first turns are the starting numbers
        yieldAll(numbers)

        // Now keep track of when a number was last seen
        // Skip the last item from the starting numbers list, as that is the number to consider
        // in the first iteration
        val lastSeen = numbers.dropLast(1).mapIndexed { idx, i -> i to idx }.toMap().toMutableMap()
        var turn = numbers.size - 1
        var previous = numbers.last()

        while (true) {
            var newValue = if (!lastSeen.containsKey(previous)) {
                0
            } else {
                turn - lastSeen[previous]!!
            }

            yield(newValue)

            lastSeen[previous] = turn++
            previous = newValue
        }
    }

    override fun part1() = game.take(2020).last()
    override fun part2() = game.take(30000000).last()
}