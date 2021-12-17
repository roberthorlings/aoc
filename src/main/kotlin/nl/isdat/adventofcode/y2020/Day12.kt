package nl.isdat.adventofcode.y2020

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Direction
import nl.isdat.adventofcode.Input.fileAsList
import nl.isdat.adventofcode.Point


class Day12 : Day() {
    val instructions = fileAsList("2020/day12.txt").map(::parseInstruction)

    override fun part1(): Int {
        val origin = Point(0, 0)
        var position = origin
        var direction = Direction.EAST

        instructions.forEach { instruction ->
            when(instruction.operation) {
                is Operation.Move -> position += (instruction.operation.direction.vector * instruction.count)
                is Operation.Turn -> repeat(instruction.count / 90) {
                    when (instruction.operation.side) {
                        Side.LEFT -> direction = direction.turnLeft()
                        Side.RIGHT -> direction = direction.turnRight()
                    }
                }
                is Operation.Forward -> position += (direction.vector * instruction.count)
            }
        }

        return position.manhattan(origin)
    }

    override fun part2(): Int {
        val origin = Point(0, 0)
        var waypoint = origin + Direction.NORTH.vector + Direction.EAST.vector * 10
        var position = origin

        instructions.forEach { instruction ->
            when(instruction.operation) {
                is Operation.Move -> waypoint += (instruction.operation.direction.vector * instruction.count)
                is Operation.Turn -> repeat(instruction.count / 90) {
                    when (instruction.operation.side) {
                        Side.LEFT -> waypoint = waypoint.rotateCounterClockwise()
                        Side.RIGHT -> waypoint = waypoint.rotateClockwise()
                    }
                }
                is Operation.Forward -> position += (waypoint * instruction.count)
            }
        }

        return position.manhattan(origin)
    }

    fun parseInstruction(input: String): Instruction {
        val op = input[0]
        val count = input.drop(1).toInt()

        return Instruction(
            when(input[0]) {
                'N' -> Operation.Move(Direction.NORTH)
                'E' -> Operation.Move(Direction.EAST)
                'S' -> Operation.Move(Direction.SOUTH)
                'W' -> Operation.Move(Direction.WEST)
                'L' -> Operation.Turn(Side.LEFT)
                'R' -> Operation.Turn(Side.RIGHT)
                'F' -> Operation.Forward
                else -> throw IllegalStateException("Invalid instruction: " + input)
            },
            count
        )
    }

    data class Instruction(val operation: Operation, val count: Int)

    sealed class Operation {
        data class Move(val direction: Direction): Operation() {
            override fun toString(): String = "Move(${direction})"
        }
        data class Turn(val side: Side): Operation() {
            override fun toString(): String = "Turn(${side})"
        }
        object Forward: Operation() {
            override fun toString(): String = "Forward"
        }
    }

    enum class Side { LEFT, RIGHT }

}