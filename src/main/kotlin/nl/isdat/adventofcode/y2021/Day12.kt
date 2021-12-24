package nl.isdat.adventofcode.y2021

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input

class Day12(private val fileName: String = "2021/day12.txt"): Day() {
    val graph = Input.fileAsList(fileName)
        .flatMap {
            val parts = it.split("-")
            listOf(
                parts[0] to parts[1],
                parts[1] to parts[0]
            )
        }

    fun hasPath(start: String, end: String) = graph.contains(start to end)
    fun incoming(node: String) = graph.filter { it.second == node }.map { it.first }

    fun numPathsBetweenSingle(start: String, end: String, avoid: List<String> = emptyList()): Long {
        val directPaths = if(hasPath(start, end)) 1 else 0
        val indirectPaths = incoming(end)
            .filterNot { node -> avoid.contains(node)}
            .filterNot { node -> node == start}
            .map { node ->
                val isSmallCave = end[0].isLowerCase()
                numPathsBetweenSingle(
                    start,
                    node,
                    avoid + if(isSmallCave) listOf(end) else emptyList()
                )
            }
            .sum()

        return directPaths + indirectPaths
    }

    fun pathsBetweenWithDuplicate(start: String, end: String, avoid: List<String> = emptyList(), duplicateUsed: Boolean = false, prefix: String = ""): List<List<String>> {
        val directPaths = if(hasPath(start, end)) listOf(listOf(start, end)) else emptyList()
        val indirectPaths = incoming(end)
            .filterNot { node -> avoid.contains(node)}
            .filterNot { node -> node == start}
            .flatMap { node ->
                val isSmallCave = end[0].isLowerCase()

                if(isSmallCave) {
                    var numPaths = pathsBetweenWithDuplicate(
                        start,
                        node,
                        avoid + end,
                        duplicateUsed,
                        "${prefix}  "
                    )

                    if (!duplicateUsed && end != "end") {
                        numPaths + pathsBetweenWithDuplicate(
                            start,
                            node,
                            avoid,
                            true,
                            "${prefix}  "
                        )
                    } else {
                        numPaths
                    }
                } else {
                    pathsBetweenWithDuplicate(
                        start,
                        node,
                        avoid,
                        duplicateUsed,
                        "${prefix}  "
                    )
                }
            }
            .map {
                it + end
            }

        return (directPaths + indirectPaths).distinct()
    }

    override fun part1(): Long = numPathsBetweenSingle("start", "end")

    override fun part2(): Long = pathsBetweenWithDuplicate("start", "end").size.toLong()

}