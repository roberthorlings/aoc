package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsList
import java.math.BigInteger
import kotlin.math.pow


class Day14 : Day() {
    val lines = fileAsList("2020/day14.txt")

    override fun part1(): Any {
        var bitmask = BitMaskV1("")
        val memory = mutableMapOf<Int, Long>()

        lines.forEach { line ->
            val (a, b) = line.split(" = ").map(String::trim)

            if(a == "mask") {
                bitmask = BitMaskV1(b)
            } else {
                val memAddress = a.replace("mem[", "").replace("]", "").toInt()
                memory[memAddress] = bitmask.apply(b.toLong())
            }

        }

        return memory.values.sum()
    }

    override fun part2(): Any {
        var bitmask = BitMaskV2("X")
        val memory = mutableMapOf<BigInteger, Long>()

        lines.forEach { line ->
            val (a, b) = line.split(" = ").map(String::trim)
            if(a == "mask") {
                bitmask = BitMaskV2(b)
            } else {
                val memAddress = a.replace("mem[", "").replace("]", "").toInt()
                bitmask.apply(memAddress.toBigInteger()).forEach {
                    memory[it] = b.toLong()
                }
            }
        }

        println("# memory slots filled: ${memory.size}")
        println("Largest value: ${memory.values.maxOrNull()}")
        return memory.values.sum()
    }

    data class BitMaskV1(val mask: String) {
        val zerosMask = mask.reversed().mapIndexed { idx, char -> if(char != '0') (2.0).pow(idx).toLong() else 0L }.sum()
        val onesMask = mask.reversed().mapIndexed { idx, char -> if(char == '1') (2.0).pow(idx).toLong() else 0L }.sum()

        fun apply(value: Long) = value.and(zerosMask).or(onesMask)
    }

    data class BitMaskV2(val mask: String) {
        val onesMask = mask.reversed().mapIndexed { idx, char -> if (char == '1') (2.0).pow(idx).toBigDecimal().toBigInteger() else BigInteger.ZERO }.reduce { a, b -> a + b }
        val crosses = mask.reversed().withIndex().filter { it.value == 'X' }.map { it.index }

        fun apply(value: BigInteger): List<BigInteger> {
            val memoryValue = value.or(onesMask)
            println("  - original:  ${value.toString(2).padStart(36)}")
            println("  - with ones: ${memoryValue.toString(2).padStart(36)}")
            return  crosses.fold(listOf(memoryValue))
                    { list: List<BigInteger>, idx: Int ->
                        // Create 2 bitmasks: one with all ones, but a zero on the index position
                        // And one with all zeros but a one on the index position and use them
                        // to set the bit for the given number
                        val zerosBitMask = (2.0).pow(idx).toBigDecimal().toBigInteger()
                        val onesBitMask = zerosBitMask.toString(2).padStart(36, '0')
                            .replace("0", "X")
                            .replace("1", "0")
                            .replace("X", "1")
                            .toBigInteger(2)

                        println("    - idx ${idx.toString()} / ${onesBitMask.toString(2).padStart(36)} / ${zerosBitMask.toString(2).padStart(36)}")

                        list.flatMap { previous -> listOf(previous.and(onesBitMask), previous.or(zerosBitMask)) }
                    }
        }

    }
}