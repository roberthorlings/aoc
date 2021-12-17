package nl.isdat.adventofcode.y2020

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsList


class Day13 : Day() {
    val lines = fileAsList("2020/day13.txt")

    override fun part1(): Long {
        val initialTimestamp = lines[0].toLong()
        val busses = lines[1].split(",").filterNot { it == "x" }.map(String::toInt)

        var timestamp = initialTimestamp

        // Avoid an infinite loop
        while(timestamp < 2 * initialTimestamp) {
            val bus = busses.firstOrNull { timestamp % it == 0L }
            if(bus != null) {
                return bus * (timestamp - initialTimestamp)
            }

            timestamp++
        }

        return 0
    }

    override fun part2(): Long {
        val busses = lines[1].split(",")
            .mapIndexedNotNull { idx, bus ->
                if (bus == "x")
                    null
                else
                    bus.toInt() to idx
            }
            .toMap()

        var departure = 0L
        var timeIncrement = 1L

        busses.forEach { (bus, offset) ->
            // Search for a departure time that works for the current bus.
            while((departure + offset) % bus != 0L) {
                departure += timeIncrement
            }

            // If we have found one, make sure that the increment is divisable by the current bus number,
            // making sure that all subsequence departure times will also match the current bus
            timeIncrement *= bus
        }


        return departure
    }


}