package nl.isdat.adventofcode.y2020

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Direction
import nl.isdat.adventofcode.Input.fileAsString
import nl.isdat.adventofcode.Matrix
import java.lang.IllegalArgumentException

class Day20 : Day() {
    val input = fileAsString("2020/day20.txt")
    val tiles = parse(input)

    val PATTERN = listOf(
        "                  # ".toList(),
        "#    ##    ##    ###".toList(),
        " #  #  #  #  #  #   ".toList()
    )

    val PATTERN_HEIGHT = PATTERN.size
    val PATTERN_WIDTH = PATTERN[0].size

    override fun part1(): Long =
        corners()
            .take(4)
            .fold(1L, { a, b -> a * b.number })

    override fun part2(): Int {
        // Start generating the image, starting from this top left corner
        val imageTiles = arrangeTiles(tiles)

        // Remove borders from each tile and concatenate the contents
        val image = imageTiles.flatMap { row ->
            row
                .map { it.withoutBorders().matrix.contents }
                .reduce { a, b -> a.mapIndexed { idx, row -> row + b[idx] } }
        }

        // Find the orientation that has any matches
        val matrix = Matrix(image)
        val orientations = listOf(
            matrix,
            matrix.rotateLeft(),
            matrix.rotateLeft().rotateLeft(),
            matrix.rotateRight(),
            matrix.flipHorizontally(),
            matrix.flipHorizontally().rotateLeft(),
            matrix.flipHorizontally().rotateLeft().rotateLeft(),
            matrix.flipHorizontally().rotateRight()
        )

        val numMonsters = orientations.asSequence()
            .map { countPatternMatches(it.contents) }
            .first { it > 0 }

        // Return the appropriate number
        val numHashes = image.sumBy { it.count { char -> char == '#' } }
        val patternHashes = PATTERN.sumBy { it.count { char -> char == '#' } }
        return numHashes - numMonsters * patternHashes
    }

    private fun countPatternMatches(image: List<List<Char>>): Int {
        // Count occurrences of the pattern. We assume the monsters do not overlap
        val imageWidth = image[0].size
        return image.windowed(PATTERN_HEIGHT)
            .map { rows ->
                // We have 3 rows of the image now.
                (0..imageWidth - PATTERN_WIDTH)
                    .count { idx ->
                        matchesPattern(rows.map { it.slice(idx until idx + PATTERN_WIDTH) })
                    }
            }
            .sum()
    }

    private fun matchesPattern(imagePart: List<List<Char>>): Boolean {
        if(imagePart.size != PATTERN_HEIGHT || imagePart[0].size != PATTERN_WIDTH) {
            throw IllegalArgumentException("Invalid image given. Dimensions ${PATTERN_WIDTH}x${PATTERN_HEIGHT} expected")
        }

        // For each row in the image, see if the pattern matches the image
        return PATTERN.zip(imagePart)
            .all { (patternRow, imageRow) ->
                patternRow.zip(imageRow)
                    .all { (pattern, image) ->
                        pattern != '#' || pattern == image
                    }
            }
    }

    private fun arrangeTiles(tiles: List<Day20.Tile>): List<List<Tile>> {
        // Find any corner
        var corner = corners().first()

        // Rotate the corner to have the edge top left
        while(corner.findCorrespondingTile(Direction.NORTH, tiles) != null || corner.findCorrespondingTile(Direction.WEST, tiles) != null) {
            corner = corner.rotateLeft()
        }

        val imageTiles = mutableListOf<List<Tile>>()
        var current: Tile? = corner
        var start = System.currentTimeMillis()
        while(current != null && System.currentTimeMillis() - start < 5000) {
            val row = mutableListOf(current)
            var eastNeighbour = current.findCorrespondingTile(Direction.EAST, tiles)

            while(eastNeighbour != null && System.currentTimeMillis() - start < 5000) {
                val transformed = transformNeighbour(row.last(), eastNeighbour, Direction.EAST)
                row.add(transformed)
                eastNeighbour = transformed.findCorrespondingTile(Direction.EAST, tiles)
            }

            imageTiles.add(row)
            current = row.first().findCorrespondingTile(Direction.SOUTH, tiles)

            if(current != null)
                current = transformNeighbour(row.first(), current, Direction.SOUTH)

        }

        return imageTiles
    }

    /**
     * @param current Current tile
     * @param neighbour Tile to place adjacent to the current tile
     * @param neighbourOrientation Place where the neighbour should be placed, when looking from the current. I.e. EAST means that the neighbour is placed east of the current tile
     */
    private fun transformNeighbour(current: Tile, neighbour: Tile, neighbourOrientation: Direction): Tile {
        val currentMatchingEdge = current.edges[neighbourOrientation]

        // Rotate the neighbour so that it matches the previous tile
        val correspondingEdge = neighbour.edges.entries.first { (direction, edge) -> edge == currentMatchingEdge || edge.reversed() == currentMatchingEdge}

        // The corresponding edge must become the west edge
        var transformed = neighbour
        val degreesToTurn = (neighbourOrientation.opposed().degrees - correspondingEdge.key.degrees + 360) % 360
        when(degreesToTurn) {
            90 -> transformed = neighbour.rotateRight()
            180 -> transformed = neighbour.rotateLeft().rotateLeft()
            270 -> transformed = neighbour.rotateLeft()
        }

        // The tile may be flipped
        if(transformed.edges[neighbourOrientation.opposed()] != currentMatchingEdge) {
            when(neighbourOrientation) {
                Direction.EAST, Direction.WEST -> transformed = transformed.flipVertically()
                Direction.NORTH, Direction.SOUTH -> transformed = transformed.flipHorizontally()

            }
        }

        // At this point, the edges must match.
        if(transformed.edges[neighbourOrientation.opposed()] != currentMatchingEdge) {
            println("Current:\n${current}")
            println("Transformed neighbour:\n${transformed}")
            println("Direction: ${neighbourOrientation}")
            throw IllegalStateException("Edges do not match after transformation")
        }

        return transformed
    }

    // Find the tiles that have 2 edges not present in any of the other tiles
    private fun corners() =
        tiles.asSequence()
            .filter { tile ->
                tile.edges.count { (direction, _) -> tile.findCorrespondingTile(direction, tiles) != null } == 2
            }

    private fun parse(input: String): List<Tile> =
        input.split("\n\n")
            .map(::parseTile)

    private fun parseTile(tileData: String): Tile {
        val lines = tileData.lines()
        val tileNumber = lines[0].replace("Tile ", "").replace(":", "").trim().toLong()
        return Tile(tileNumber, Matrix(lines.drop(1).map(String::toList)))
    }

    data class Tile(val number: Long, val matrix: Matrix<Char>) {
        val edges: Map<Direction, List<Char>>
            get() = matrix.edges

        val edgeList: Collection<List<Char>>
            get() = matrix.edges.values

        fun findCorrespondingTile(direction: Direction, tiles: List<Tile>) =
            tiles.firstOrNull { other ->
                other != this && (other.edgeList.contains(matrix.edges[direction]) || other.edgeList.contains(matrix.edges[direction]?.reversed()))
            }

        fun rotateLeft() = Tile(number, matrix.rotateLeft())
        fun rotateRight() = Tile(number, matrix.rotateRight())
        fun flipHorizontally() = Tile(number, matrix.flipHorizontally())
        fun flipVertically() = Tile(number, matrix.flipVertically())

        fun withoutBorders() = Tile(number, Matrix(
            matrix.contents.drop(1).dropLast(1).map { it.drop(1).dropLast(1) }
        ))

        override fun equals(other: Any?): Boolean = other is Tile && other.number == number
        override fun toString(): String = "Tile: ${number}\n" + matrix.contents.map { it.joinToString("") }.joinToString("\n")
    }

    companion object {
    }
}
