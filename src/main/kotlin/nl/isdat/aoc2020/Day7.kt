package nl.isdat.aoc2020

import nl.isdat.aoc2020.Utils.fileAsSequence

object Day7 {
    @JvmStatic
    fun main(args: Array<String>) {
        val bags = fileAsSequence("day7.txt").map(::parse).toList()

        part1(bags)
        part2(bags)
    }

    private fun part1(bags: List<BagDefinition>) {
        val open = mutableListOf("shiny gold")
        val found = mutableListOf<String>()

        while (open.isNotEmpty()) {
            val current = open.first()
            val containers = getContainers(bags, current).map { it.name }

            // Containers that were found before, should not be evaluated again to avoid recursion
            val eligibleContainers = containers - found

            // The containers should be included in the found list
            found += eligibleContainers

            // Update the open list to continue searching
            open -= current
            open += eligibleContainers
        }

        println("Part 1 result: " + found.size)
    }

    private fun part2(bags: List<BagDefinition>) {
        val bagMap = bags.associateBy { it.name }
        val search = "shiny gold"

        // -1 to account for the original shiny gold bag counted
        val answer = getNumContained(bagMap, search) - 1
        println("Part 2 result: " + answer)
    }

    private fun getContainers(bags: List<BagDefinition>, bag: String) =
        bags.filter { it.containment.any { it is Containment.Bag && it.name == bag } }

    private fun getNumContained(bags: Map<String, BagDefinition>, bag: String, prefix: String = ""): Int {
        val result = 1 + bags[bag]!!.containment
            .map {
                when (it) {
                    is Containment.Empty -> 0
                    is Containment.Bag -> it.count * getNumContained(bags, it.name, prefix + "  ")
                }
            }
            .sum()

        return result
    }


    private fun parse(input: String): BagDefinition =
        input.split("contain").let {
            val container = parseBag(it[0])
            val contained = it[1].replace(".", "").split(",").map(::parseContainment)
            BagDefinition(container, contained)
        }

    private fun parseBag(input: String) = input.replace(" bags", "").replace(" bag", "").trim()
    private fun parseContainment(input: String): Containment {
        return CONTAINMENT_REGEX.find(input.replace(" bags", "").replace(" bag", ""))
            ?.destructured
            ?.let { (count, name) -> Containment.Bag(count.toInt(), name.trim()) }
            ?: Containment.Empty
    }

    data class BagDefinition(val name: String, val containment: List<Containment>)

    sealed class Containment {
        object Empty : Containment()
        class Bag(val count: Int, val name: String) : Containment()
    }

    val CONTAINMENT_REGEX = """(\d+) (.+)""".toRegex()

}