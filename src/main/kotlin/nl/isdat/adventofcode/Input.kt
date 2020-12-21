package nl.isdat.adventofcode

import java.nio.charset.Charset

object Input {
    fun fileAsReader(filename: String) = javaClass.classLoader.getResourceAsStream(filename).bufferedReader(Charset.defaultCharset())
    fun fileAsSequence(filename: String) = fileAsReader(filename).lineSequence()
    fun fileAsList(filename: String) = fileAsSequence(filename).toList()
    fun fileAsString(filename: String) = fileAsReader(filename).lines().reduce { a, b -> "${a}\n${b}" }.orElse("")
}