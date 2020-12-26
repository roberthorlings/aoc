package nl.isdat.adventofcode.`2020`

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsList

class Day18 : Day() {
    val expressions = fileAsList("2020/day18.txt").map(::tokenize)

//    override fun part1(): Long = expressions.map {
//        val result = it.evaluate()
//        println("${it} = ${result}")
//        result.toLong()
//    }.sum()

    override fun part2(): Long = expressions.map {
        val result = it.evaluate2()
        println("${it} = ${result}")
        result.toLong()
    }.sum()

    private fun tokenize(input: String): Expression =
        Expression(
            input.mapNotNull {
                    when(it) {
                        '+' -> Token.Add
                        '*' -> Token.Multiply
                        '(' -> Token.LeftParenthesis
                        ')' -> Token.RightParenthesis
                        else -> if(it.isDigit()) Token.Literal(it.toLong() - 48) else null
                    }
                }
        )

    class Expression(val tokens: List<Token>) {
        override fun toString(): String = tokens.toString()

        fun evaluate(): Long {
            var result = 0L
            var operator: OperatorType = OperatorType.ADD

            var i = 0
            while (i < tokens.size) {
                val token = tokens[i]

                // Already increase the counter for the next iteration. Please note that it may
                // be altered when parenthesis are encountered
                i++

                when (token) {
                    is Token.Add -> operator = OperatorType.ADD
                    is Token.Multiply -> operator = OperatorType.MULTIPLY
                    is Token.Literal -> when (operator) {
                        OperatorType.ADD -> result += token.value
                        OperatorType.MULTIPLY -> result *= token.value
                    }
                    is Token.LeftParenthesis -> {
                        // Extract the expression within parenthesis and compute that value first
                        val right = findMatchingParenthesis(i - 1, tokens)

                        // Otherwise, extract the expression within parenthesis, and recursively call this method
                        val expressionResult = Expression(tokens.subList(i, right)).evaluate()

                        when (operator) {
                            OperatorType.ADD -> result += expressionResult
                            OperatorType.MULTIPLY -> result *= expressionResult
                        }

                        // Continue reading after the last parenthesis
                        i = right
                    }
                }
            }

            return result
        }

        private fun findMatchingParenthesis(left: Int, tokens: List<Token>): Int {
            var right = left + 1
            var depth = 1
            while (depth > 0 && right < tokens.size) {
                when (tokens[right]) {
                    is Token.LeftParenthesis -> depth++
                    is Token.RightParenthesis -> depth--
                }
                right++
            }

            // If the depth is not 0, we did not find a matching right parenthesis
            return if(depth == 0) right - 1 else throw IllegalStateException("Invalid expression given. No matching right parenthesis")
        }

        fun evaluate2(input: List<Token> = tokens): Long {
            var tokenList = input

            // See if there are any parenthesis in there. If so, replace the value within
            // parenthesis with the literal value
            while(tokenList.contains(Token.LeftParenthesis)) {
                val left = tokenList.indexOf(Token.LeftParenthesis)
                val right = findMatchingParenthesis(left, tokenList)

                tokenList = tokenList.subList(0, left) + Token.Literal(evaluate2(tokenList.subList(left + 1, right))) + tokenList.subList(right + 1, tokenList.size)
            }

            // See if there are any additions in the list
            while(tokenList.contains(Token.Add)) {
                // If so, perform the addition
                val idx = tokenList.indexOf(Token.Add)
                val op1 = tokenList[idx - 1] as Token.Literal
                val op2 = tokenList[idx + 1] as Token.Literal
                tokenList = tokenList.subList(0, idx - 1) + Token.Literal(op1.value + op2.value) + tokenList.subList(idx + 2, tokenList.size)
            }

            // Finally,see if there are any additions in the list
            while(tokenList.contains(Token.Multiply)) {
                // If so, perform the addition
                val idx = tokenList.indexOf(Token.Multiply)
                val op1 = tokenList[idx - 1] as Token.Literal
                val op2 = tokenList[idx + 1] as Token.Literal
                tokenList = tokenList.subList(0, idx - 1) + Token.Literal(op1.value * op2.value) + tokenList.subList(idx + 2, tokenList.size)
            }

            // We should be left with a single literal value now
            if (tokenList.size != 1 || tokenList[0] !is Token.Literal) throw java.lang.IllegalStateException("The algorithm did not result in a single literal value")

            return (tokenList[0] as Token.Literal).value
        }
    }

    sealed class Token {
        object Add: Token() { override fun toString() = " + " }
        object Multiply: Token() { override fun toString() = " * " }
        object LeftParenthesis: Token() { override fun toString() = "(" }
        object RightParenthesis: Token() { override fun toString() = ")" }
        class Literal(val value: Long): Token() {
            override fun toString(): String = value.toString()
        }
    }

    enum class OperatorType { ADD, MULTIPLY }
}
