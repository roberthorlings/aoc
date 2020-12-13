package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsSequence

class Day5: Day() {
    val seats = fileAsSequence("2020/day5.txt").map { parse(it) }.toList()

    override fun part1(): Int {
        val seat = seats.maxByOrNull { it.seatID } ?: throw java.lang.IllegalArgumentException("Empty list of seats given")
        return seat.seatID
    }

    override fun part2(): Int {
        val rows = seats.map { it.row }
        val columns = seats.map { it.column }

        val possibleRows = rows.min()!!..rows.max()!!
        val possibleColumns = columns.min()!!..columns.max()!!

        outer@ for(r in possibleRows) {
            for(c in possibleColumns) {
                val seat = Seat(r, c)
                if(
                    !seats.contains(seat)
                    && seats.contains(Seat(r - 1, c))
                    && seats.contains(Seat(r + 1, c))
                ) {
                    return seat.seatID
                }
            }
        }

        return 0
    }

    private fun parse(input: String): Seat =
        Seat(
            parseRow(input.substring(0..6)),
            parseColumn(input.substring(7..9))
        )

    private fun parseRow(input: String) = input.map {
        when (it) {
            'F' -> 0
            'B' -> 1
            else -> throw IllegalArgumentException("Invalid row character: " + it)
        }
    }.joinToString("").toInt(2)

    private fun parseColumn(input: String) = input.map {
        when (it) {
            'L' -> 0
            'R' -> 1
            else -> throw IllegalArgumentException("Invalid column character: " + it)
        }
    }.joinToString("").toInt(2)

    data class Seat(val row: Int, val column: Int) {
        val seatID = row * 8 + column
    }

}