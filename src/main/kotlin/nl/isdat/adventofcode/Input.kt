package nl.isdat.adventofcode

import java.nio.charset.Charset

object Input {

    fun fileAsSequence(filename: String) = javaClass.classLoader.getResourceAsStream(filename).bufferedReader(Charset.defaultCharset()).lineSequence()
    fun fileAsList(filename: String) = fileAsSequence(filename).toList()
}