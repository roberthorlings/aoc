package nl.isdat.adventofcode.y2021

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Grid
import nl.isdat.adventofcode.Input
import nl.isdat.adventofcode.Point

class Day13(private val fileName: String = "2021/day13.txt"): Day() {
    data class Fold(val axis: String, val value: Int)

    fun parseInput(): Pair<List<Point>, List<Fold>> {
        val lines = Input.fileAsList(fileName)

        val points = lines
                .takeWhile { it.isNotEmpty() }
                .map {
                    val (x,y) = it.split(",").map(String::toInt)
                    Point(x,y)
                }

        val folds = lines
            .takeLastWhile { it.isNotEmpty() }
            .map { line ->
                val fold = line.split(" ")[2].split("=")
                Fold(fold[0], fold[1].toInt())
            }

        return points to folds
    }

    fun foldX(point: Point, foldValue: Int) =
        if(point.x > foldValue) {
            Point(2 * foldValue - point.x, point.y)
        } else {
            point
        }

    fun foldY(point: Point, foldValue: Int) =
        if(point.y > foldValue) {
            Point(point.x, 2 * foldValue - point.y)
        } else {
            point
        }

    fun fold(points: List<Point>, fold: Fold): List<Point> {
        val folded = points.map { point ->
            when (fold.axis) {
                "x" -> foldX(point, fold.value)
                "y" -> foldY(point, fold.value)
                else -> throw IllegalArgumentException("Invalid axis: " + fold.axis)
            }
        }.distinct()

        println("${folded.size} points after folding ${fold}: ${folded}")

        return folded
    }

    fun printPoints(points: List<Point>) {
        val grid = Grid.fromEntries(points.map { it to "#"}.toMap(), ".")
        println(grid)
    }

    override fun part1(): Long {
        val (initialPoints, folds) = parseInput()
        return fold(initialPoints, folds[0]).size.toLong()
        //return folds.fold(initialPoints, this::fold).size.toLong()
    }

    override fun part2(): Long {
        val (initialPoints, folds) = parseInput()
        val result = folds.fold(initialPoints, this::fold)

        printPoints(result)
        return 0;

    }

}