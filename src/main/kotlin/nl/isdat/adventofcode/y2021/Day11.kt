package nl.isdat.adventofcode.y2021

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Grid
import nl.isdat.adventofcode.Input

class Day11(private val fileName: String = "2021/day11.txt", private val numSteps: Int = 100): Day() {
    data class Octopus(
        val energy: Int,
        val hasFlashed: Boolean = false
    ) {
        override fun toString(): String = energy.toString()
    }

    data class StepResult(
        val grid: Grid<Octopus>,
        val numFlashes: Int = 0
    )

    val initialGrid = Input.fileAsList(fileName).let { lines ->
        Grid(lines.map {
            it.toList().map {
                Octopus(Character.getNumericValue(it))
            }
        })
    }

    override fun part1(): Long =
        (1..numSteps)
            .fold(StepResult(initialGrid)) { acc, idx ->
                val result = step(acc.grid)
                println("After step ${idx}, ${result.numFlashes} flashes")
                result.copy(numFlashes = result.numFlashes + acc.numFlashes)
            }.numFlashes.toLong()

    override fun part2(): Long {
        val numOctopuses = initialGrid.size

        var currentGrid = initialGrid
        var stepNumber = 1

        while (true) {
            val result = step(currentGrid)
            if(result.numFlashes == numOctopuses) {
                return stepNumber.toLong()
            }

            println("Step ${stepNumber}, num flashes: ${result.numFlashes}")
            stepNumber++
            currentGrid = result.grid
        }
    }

    fun step(grid: Grid<Octopus>): StepResult {
        // Increase energy for all octopuses with 1
        var newGrid = grid.map { octopus -> octopus.copy(energy = octopus.energy + 1) }

        // Flash all octopuses with energy > 9
        var highEnergyOctopuses = newGrid.entries().filter { (k, v) -> v.energy > 9 }

        while(highEnergyOctopuses.size > 0) {
            val triggeredOctopuses = highEnergyOctopuses
                .flatMap { newGrid.neighbours(it.key) }
                .groupingBy { it }
                .eachCount()

            newGrid = newGrid
                .map { point, octopus ->
                    val newEnergy = octopus.energy + (triggeredOctopuses.get(point) ?: 0)
                    val hasFlashed = octopus.hasFlashed || highEnergyOctopuses.containsKey(point)
                    octopus.copy(
                        energy = newEnergy,
                        hasFlashed = hasFlashed
                    )
                }

            highEnergyOctopuses = newGrid.entries().filter { (k, v) -> v.energy > 9 && !v.hasFlashed }
        }

        val numFlashes = newGrid.entries().count { it.value.hasFlashed }
        val finalGrid =
            newGrid.map { octopus ->
                if (octopus.hasFlashed)
                    Octopus(0)
                else
                    octopus
            }

        return StepResult(finalGrid, numFlashes)
    }

}