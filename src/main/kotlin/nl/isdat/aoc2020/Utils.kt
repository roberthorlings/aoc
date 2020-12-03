package nl.isdat.aoc2020

import java.nio.charset.Charset

object Utils {
    fun fileAsLines(filename: String) = javaClass.classLoader.getResourceAsStream(filename).bufferedReader(Charset.defaultCharset()).lineSequence()
}