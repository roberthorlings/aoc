package nl.isdat.adventofcode

import kotlin.system.measureTimeMillis

object Runner {
    @JvmStatic
    fun main(args: Array<String>) {
        if(args.size != 2) {
            System.err.println("Usage: java Runner <year> <day>")
            return
        }

        val year = args[0].toIntOrNull()
        val day = args[1].toIntOrNull()

        if(year == null || day == null) {
            System.err.println("Usage: java Runner <year> <day>")
            return
        }

        val execution = Class.forName("nl.isdat.adventofcode.${year}.Day${day}").getDeclaredConstructor().newInstance() as Day

        execution.run()
    }
}