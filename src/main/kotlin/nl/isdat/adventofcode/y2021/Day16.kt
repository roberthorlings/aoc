package nl.isdat.adventofcode.y2021

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input

class Day16(private val input: String = Input.fileAsString("2021/day16.txt")): Day() {

    sealed class LengthTypeId {
        class TotalLength(lengthInBits: Int): LengthTypeId()
        class SubPackets(numSubPackets: Int): LengthTypeId()
    }
    sealed class Packet(val version: Int, val typeID: Int) {
        abstract fun value(): Long
        class Literal(version: Int, typeID: Int, val content: Long): Packet(version, typeID) {
            override fun value(): Long = content
        }
        class Operator(version: Int, typeID: Int, val lengthTypeId: LengthTypeId, val subPackets: List<Packet>): Packet(version, typeID) {
            fun subValues() = subPackets.map { it.value() }
            override fun value(): Long = when(typeID) {
                0 -> subValues().sum()
                1 -> subValues().reduce { a, b -> a * b }
                2 -> subValues().minOrNull() ?: throw IllegalStateException("Empty list of subpackets")
                3 -> subValues().maxOrNull() ?: throw IllegalStateException("Empty list of subpackets")
                5 -> subValues().let { if(it[0] > it[1]) 1 else 0 }
                6 -> subValues().let { if(it[0] < it[1]) 1 else 0 }
                7 -> subValues().let { if(it[0] == it[1]) 1 else 0 }
                else -> throw IllegalStateException("Invalid typeID in operator packet: ${typeID}")
            }
        }
    }

    class Parser() {
        data class PacketParseResult(val value: Packet, val bitsConsumed: Int)
        data class LiteralParseResult(val value: Long, val bitsConsumed: Int)
        data class OperatorParseResult(val lengthTypeId: LengthTypeId, val subPackets: List<Packet>, val bitsConsumed: Int)

        private val HEADER_LENGTH: Int = 6
        private val OPERATOR_BIT_LENGTH = 15
        private val OPERATOR_NUM_PACKETS_LENGTH = 11

        private val CONVERSION_MAP = mapOf(
            '0' to "0000".asSequence(),
            '1' to "0001".asSequence(),
            '2' to "0010".asSequence(),
            '3' to "0011".asSequence(),
            '4' to "0100".asSequence(),
            '5' to "0101".asSequence(),
            '6' to "0110".asSequence(),
            '7' to "0111".asSequence(),
            '8' to "1000".asSequence(),
            '9' to "1001".asSequence(),
            'A' to "1010".asSequence(),
            'B' to "1011".asSequence(),
            'C' to "1100".asSequence(),
            'D' to "1101".asSequence(),
            'E' to "1110".asSequence(),
            'F' to "1111".asSequence(),
        )

        private fun toBitSequence(input: String) = input
            .asSequence()
            .flatMap {
                CONVERSION_MAP[it] ?: throw IllegalStateException("Invalid character found in hex stream: ${it}")
            }

        private fun parseVersion(sequence: Sequence<Char>) = sequence.take(3).joinToString("").toInt(2)
        private fun parseTypeID(sequence: Sequence<Char>) = sequence.take(3).joinToString("").toInt(2)
        private fun parseLiteral(sequence: Sequence<Char>): LiteralParseResult {
            var groupFirstBit = '1'

            val bitsForValue = sequence
                .windowed(5, 5)
                .takeWhile {
                    val previousBit = groupFirstBit
                    groupFirstBit = it.first()

                    previousBit == '1'
                }
                .map { it.drop(1) }
                .toList()

            val numBitsUsedForCharacter = bitsForValue.size * 5
            val literalValue = bitsForValue.flatten().joinToString("").toLong(2)

            return LiteralParseResult(
                literalValue,
                numBitsUsedForCharacter
            )
        }

        private fun parseOperator(sequence: Sequence<Char>): OperatorParseResult =
            when(sequence.first()) {
                '0' -> {
                    parseOperatorByLengthInBits(
                        sequence.drop(1 + OPERATOR_BIT_LENGTH),
                        sequence.drop(1).take(OPERATOR_BIT_LENGTH).joinToString("").toInt(2)
                    ).let {
                        it.copy(bitsConsumed = it.bitsConsumed + OPERATOR_BIT_LENGTH + 1)
                    }
                }
                '1' -> {
                    parseOperatorByNumberOfPackets(
                        sequence.drop(1 + OPERATOR_NUM_PACKETS_LENGTH),
                        sequence.drop(1).take(OPERATOR_NUM_PACKETS_LENGTH).joinToString("").toInt(2)
                    ).let {
                        it.copy(bitsConsumed = it.bitsConsumed + OPERATOR_NUM_PACKETS_LENGTH + 1)
                    }
                }
                else -> throw IllegalStateException("Invalid LengthTypeId value")
            }


        private fun parseOperatorByNumberOfPackets(sequence: Sequence<Char>, numPackets: Int): OperatorParseResult {
            var bitsConsumed = 0
            val subPackets = mutableListOf<Packet>()

            while(subPackets.size < numPackets) {
                val (packet, packetBits) = parsePacket(sequence.drop(bitsConsumed))
                bitsConsumed += packetBits
                subPackets += packet
            }

            return OperatorParseResult(
                lengthTypeId = LengthTypeId.SubPackets(numPackets),
                subPackets = subPackets,
                bitsConsumed = bitsConsumed
            )
        }

        private fun parseOperatorByLengthInBits(sequence: Sequence<Char>, length: Int): OperatorParseResult {
            var bitsConsumed = 0
            val subPackets = mutableListOf<Packet>()

            while(bitsConsumed < length) {
                val (packet, packetBits) = parsePacket(sequence.drop(bitsConsumed))
                bitsConsumed += packetBits
                subPackets += packet
            }

            return OperatorParseResult(
                lengthTypeId = LengthTypeId.TotalLength(length),
                subPackets = subPackets,
                bitsConsumed = bitsConsumed
            )
        }

        private fun parsePacket(sequence: Sequence<Char>): PacketParseResult {
            val version = parseVersion(sequence)
            val typeID = parseTypeID(sequence.drop(3))
            val tail: Sequence<Char> = sequence.drop(HEADER_LENGTH)

            when(typeID) {
                4 -> {
                    val result = parseLiteral(tail)
                    return PacketParseResult(
                        Packet.Literal(version, typeID, result.value),
                        result.bitsConsumed + HEADER_LENGTH
                    )
                }
                else -> {
                    val result = parseOperator(tail)
                    return PacketParseResult(
                        Packet.Operator(version, typeID, result.lengthTypeId, result.subPackets),
                        result.bitsConsumed + HEADER_LENGTH
                    )
                }
            }
        }

        fun parse(input: String): Packet = parsePacket(toBitSequence(input)).value
    }

    val parsedInput = Parser().parse(input)

    override fun part1(): Long {
        fun countVersion(packet: Packet): Long = when(packet) {
            is Packet.Literal -> packet.version.toLong()
            is Packet.Operator -> packet.version + packet.subPackets.map { countVersion(it) }.sum()
        }

        return countVersion(parsedInput)
    }

    override fun part2(): Long {
        return parsedInput.value()
    }

}