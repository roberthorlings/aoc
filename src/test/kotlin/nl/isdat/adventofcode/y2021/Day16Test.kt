package nl.isdat.adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day16Test {
    @Test
    fun `should handle test input correctly for part 1`() {
        assertEquals(6, Day16("D2FE28").part1())
        assertEquals(9, Day16("38006F45291200").part1())
        assertEquals(16, Day16("8A004A801A8002F478").part1())
        assertEquals(12, Day16("620080001611562C8802118E34").part1())
        assertEquals(23, Day16("C0015000016115A2E0802F182340").part1())
        assertEquals(31, Day16("A0016C880162017C3686B18A3D4780").part1())
    }

    @Test
    fun `should handle test input correctly for part 2`() {
        assertEquals(3, Day16("C200B40A82").part2())
        assertEquals(54, Day16("04005AC33890").part2())
        assertEquals(7, Day16("880086C3E88112").part2())
        assertEquals(9, Day16("CE00C43D881120").part2())
        assertEquals(1, Day16("D8005AC2A8F0").part2())
        assertEquals(0, Day16("F600BC2D8F").part2())
        assertEquals(0, Day16("9C005AC2F8F0").part2())
        assertEquals(1, Day16("9C0141080250320F1802104A08").part2())
    }
}