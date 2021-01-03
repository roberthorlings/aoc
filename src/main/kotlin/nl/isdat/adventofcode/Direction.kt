package nl.isdat.adventofcode

enum class Direction(val vector: Point, val degrees: Int) {
    NORTH(Point(0, 1), 270),
    EAST(Point(1, 0), 0),
    SOUTH(Point(0, -1), 90),
    WEST(Point(-1, 0), 180);

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

    fun opposed(): Direction = when(this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }

}
