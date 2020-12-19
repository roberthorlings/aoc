package nl.isdat.adventofcode

enum class Direction(val vector: Point) {
    NORTH(Point(0, 1)),
    EAST(Point(1, 0)),
    SOUTH(Point(0, -1)),
    WEST(Point(-1, 0));

    fun turnRight(): Direction = when(this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun turnLeft(): Direction = when(this) {
        NORTH -> WEST
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
    }
}
