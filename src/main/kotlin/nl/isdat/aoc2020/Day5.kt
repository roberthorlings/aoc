package nl.isdat.aoc2020

import nl.isdat.aoc2020.Utils.fileAsLines

object Day5 {
    @JvmStatic
    fun main(args: Array<String>) {
        val rows = fileAsLines("day5.txt").map { parse(it) }.toList()

        part1(rows)
        part2(rows)
    }

    private fun part1(seats: List<Seat>) {
        val seat = seats.maxBy { it.seatID } ?: throw java.lang.IllegalArgumentException("Empty list of seats given")
        println("Part 1 result: " + seat.seatID)
    }

    private fun part2(seats: List<Seat>) {
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
                    println("Part 2 result: " + seat + " - " + seat.seatID )
                    break@outer
                }
            }
        }
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