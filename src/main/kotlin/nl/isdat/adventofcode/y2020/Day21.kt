package nl.isdat.adventofcode.y2020

import nl.isdat.adventofcode.Day
import nl.isdat.adventofcode.Input.fileAsList
import java.lang.IllegalArgumentException

class Day21 : Day() {
    val input = fileAsList("2020/day21.txt").map(::parse)
    val ingredients = input.flatMap { it.ingredients }.toSet()
    val allergens = input.flatMap { it.allergens }.toSet()


    // Create a map of allergen to ingredients possibly containing the allergen
    val ingredientMap = input
        .flatMap { food ->
            food.allergens.map {
                    allergen -> allergen to food.ingredients.toSet()
            }
        }
        .groupBy({ it.first }, { it.second })
        .mapValues { (key, ingredients) ->
            // Each allergen is found in exactly one ingredient, so the ingredient causing this
            // allergen must be in all lists of ingredients
            ingredients.reduce { a, b -> a.intersect(b) }
        }

    override fun part1(): Int {
        // Determine the ingredients that can't possibly contain any allergen
        val safeIngredients = ingredients - ingredientMap.values.flatten()

        return input.sumBy { food -> food.ingredients.intersect(safeIngredients).size }
    }

    override fun part2(): String {
        val allergensToCheck = ingredientMap.keys.toMutableSet()
        val association = mutableMapOf<String, String>()

        while(allergensToCheck.isNotEmpty()) {
            val(allergen, ingredients) = ingredientMap.entries
                // Only consider the open list of allergens
                .filter { (allergen, _) -> allergensToCheck.contains(allergen) }

                // Exclude the ingredients already mapped
                .map { (allergen, ingredients) -> allergen to (ingredients - association.values) }

                // Sort by number of possible ingredients
                .sortedBy { (_, possibleIngredients) -> possibleIngredients.size }

                // Take the first
                .first()

            if(ingredients.size != 1)
                throw IllegalArgumentException("No or too many ingredients to choose from for allergen ${allergen}")

            association[allergen] = ingredients.first()
            allergensToCheck -= allergen
        }

        return association.entries
                // Sort by allergen
                .sortedBy { (allergen, _) -> allergen }

                // Generate a string with all ingredients
                .map { (_, ingredient) -> ingredient }

                .joinToString(",")
    }

    private fun parse(input: String): Food {
        val(ingredients, allergens) = input.replace(")", "").split("(contains ")
        return Food(
            ingredients = ingredients.split(" ").map(String::trim).filter(String::isNotBlank),
            allergens = allergens.split(",").map(String::trim).filter(String::isNotBlank)
        )
    }

    data class Food(val ingredients: List<String>, val allergens: List<String>)
}
