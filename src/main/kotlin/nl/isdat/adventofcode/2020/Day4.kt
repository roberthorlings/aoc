package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsList

class Day4: Day() {
    val passports = parse(fileAsList("day4.txt"))

    override fun part1(): Int = passports.count { it.containsRequiredFields() }
    override fun part2(): Int = passports.count { it.containsRequiredFields() && it.matchesAllRules() }

    private fun parse(lines: List<String>): List<Passport> {
        var remaining = lines
        val passports = mutableListOf<Passport>()

        while(remaining.isNotEmpty()) {
            val passportLines = remaining.takeWhile { it.isNotEmpty() }

            passports += Passport(
                passportLines
                    .flatMap {
                        it.split(" ")
                            .map {
                                it.split(":").let { it[0] to it[1] }
                            }
                    }
                    .toMap()
            )

            remaining = remaining.drop(passportLines.size + 1)
        }

        return passports
    }

    data class Passport(val fields: Map<String, String>) {
        val REQUIRED_FIELDS = listOf(
            "byr",
            "iyr",
            "eyr",
            "hgt",
            "hcl",
            "ecl",
            "pid",
            // "cid"
        )

        fun containsRequiredFields() = fields.keys.containsAll(REQUIRED_FIELDS)
        fun matchesAllRules() = rules.all { (key, validator) -> fields[key]?.let(validator) ?: true  }

        val rules: Map<String, (String) -> Boolean> = mapOf(
            "byr" to { it.toIntOrNull()?.let { it >= 1920 && it <= 2002 } ?: false },
            "iyr" to { it.toIntOrNull()?.let { it >= 2010 && it <= 2020 } ?: false },
            "eyr" to { it.toIntOrNull()?.let { it >= 2020 && it <= 2030 } ?: false },
            "hgt" to {
                val value = it.takeWhile { it.isDigit() }.toIntOrNull()
                val unit = it.dropWhile { it.isDigit() }

                when(unit) {
                    "cm" -> value?.let { it >= 150 && it <= 193 } ?: false
                    "in" -> value?.let { it >= 59 && it <= 76 } ?: false
                    else -> false
                }
            },
            "hcl" to { it.matches("""#[0-9a-f]{6}""".toRegex()) },
            "ecl" to { listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(it) },
            "pid" to { it.all { it.isDigit() } && it.length == 9 }
        )
    }
}