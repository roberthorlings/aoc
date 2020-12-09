package nl.isdat.aoc2020

import java.nio.charset.Charset

object Utils {
    fun fileAsSequence(filename: String) = javaClass.classLoader.getResourceAsStream(filename).bufferedReader(Charset.defaultCharset()).lineSequence()
    fun fileAsList(filename: String) = fileAsSequence(filename).toList()
}