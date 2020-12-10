package nl.isdat.aoc2020

import nl.isdat.aoc2020.Utils.fileAsSequence

object Day8 {
    @JvmStatic
    fun main(args: Array<String>) {
        val instructions = fileAsSequence("day8.txt").map(::parse).toList()

        part1(instructions)
        part2(instructions)
    }

    private fun part1(instructions: List<Instruction>) {
        val stateMachine = StateMachine(instructions)
        stateMachine.run()
        println("Part 1 result: " + stateMachine.accumulator)
    }

    private fun part2(instructions: List<Instruction>) {
        (0 until instructions.size).forEach { idx ->
            val instruction = instructions[idx]

            val newInstruction = when(instruction.operation) {
                Instruction.Operation.ACC -> instruction
                Instruction.Operation.JMP -> Instruction(Instruction.Operation.NOP, instruction.operand)
                Instruction.Operation.NOP -> Instruction(Instruction.Operation.JMP, instruction.operand)
            }

            if (newInstruction != instruction) {
                val stateMachine = StateMachine(instructions.mapIndexed { index, instruction -> if(index == idx) newInstruction else instruction })

                stateMachine.run()

                if(stateMachine.isFinished()) {
                    println("Part 2 result: " + stateMachine.accumulator)
                }
            }
        }

    }

    private fun parse(input: String): Instruction =
        input.split(" ").let {
            Instruction(
                Instruction.Operation.valueOf(it[0].toUpperCase()),
                it[1].toInt()
            )
        }

    class StateMachine(val instructions: List<Instruction>) {
        var position: Int = 0
        var accumulator: Int = 0

        fun step() {
            val instruction = instructions[position]
            when(instruction.operation) {
                Instruction.Operation.ACC -> {
                    accumulator += instruction.operand
                    position += 1
                }
                Instruction.Operation.JMP -> position += instruction.operand
                Instruction.Operation.NOP -> position += 1
            }
        }

        fun isFinished() = position == instructions.size

        fun run() {
            val positionsExecuted = mutableListOf<Int>()

            while(!positionsExecuted.contains(position) && !isFinished()) {
                positionsExecuted += position
                step()
            }
        }
    }

    data class Instruction(val operation: Operation, val operand: Int) {
        enum class Operation {
            ACC,
            JMP,
            NOP
        }
    }
}