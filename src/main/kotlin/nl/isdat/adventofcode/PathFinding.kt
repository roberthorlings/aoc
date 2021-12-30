package nl.isdat.adventofcode

class PathFinding<T>(
    val neighbours: (T) -> Set<T>,
    val edgeScore: (T, T) -> Int
) {
    private fun reconstructPath(cameFrom: Map<T, T>, current: T): List<T> {
        var totalPath = listOf(current)
        var node = current

        while(cameFrom.containsKey(node)) {
            node = cameFrom[node]!!
            totalPath = listOf(node) + totalPath
        }

        return totalPath
    }

    /**
     * A* algorithm
     * @param heuristic h(n) estimates the cost to reach goal from node n.
     * @see https://en.wikipedia.org/wiki/A*_search_algorithm
     */
    fun aStar(start: T, goal: T, heuristic: (T) -> Int): List<T> {
        // The set of discovered nodes that may need to be (re-)expanded.
        // Initially, only the start node is known.
        // This is usually implemented as a min-heap or priority queue rather than a hash-set.
        val openSet = mutableSetOf(start)

        // For node n, cameFrom[n] is the node immediately preceding it on the cheapest path from start
        // to n currently known.
        val cameFrom = mutableMapOf<T, T>()

        // For node n, gScore[n] is the cost of the cheapest path from start to n currently known.
        val gScore = mutableMapOf<T, Int>().withDefault { Int.MAX_VALUE }
        gScore[start] = 0

        // For node n, fScore[n] := gScore[n] + h(n). fScore[n] represents our current best guess as to
        // how short a path from start to finish can be if it goes through n.
        val fScore = mutableMapOf<T, Int>().withDefault { Int.MAX_VALUE }
        fScore[start] = heuristic(start)

        while(openSet.isNotEmpty()) {
            val current = openSet.minByOrNull { fScore.getValue(it) }!!

            if(current == goal) return reconstructPath(cameFrom, current)

            openSet -= current

            for(neighbour in neighbours(current)) {
                // d(current,neighbor) is the weight of the edge from current to neighbor
                // tentative_gScore is the distance from start to the neighbor through current
                val tentativeGScore = gScore.getValue(current) + edgeScore(current, neighbour)

                if(tentativeGScore < gScore.getValue(neighbour)) {
                    // This path to neighbor is better than any previous one. Record it!
                    cameFrom[neighbour] = current
                    gScore[neighbour] = tentativeGScore
                    fScore[neighbour] = tentativeGScore + heuristic(neighbour)
                    if(!openSet.contains(neighbour)) {
                        openSet.add(neighbour)
                    }
                }
            }
        }
        throw IllegalStateException("Path not found")
    }
}