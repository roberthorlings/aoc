package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsSequence

class Day8: Day() {
    val instructions = fileAsSequence("2020/day8.txt").map(::parse).toList()

    override fun part1(): Int {
        val stateMachine = StateMachine(instructions)
        stateMachine.run()
        return stateMachine.accumulator
    }

    override fun part2(): Int {
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
                    return stateMachine.accumulator
                }
            }
        }

        System.err.println("The state machine never finished!")
        return 0
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