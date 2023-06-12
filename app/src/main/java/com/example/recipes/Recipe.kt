package com.example.recipes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Serializable
data class ResponseObject(
    val recipeID: Int,
    val category: String,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<Long>,
    val image: String
)

class Recipe(category: String, name: String, recipe: String, steps: List<Long>, image: String) {

    private val category = category
    private val name = name
    private val recipe = recipe
    private val steps = steps
    private val image = image

    companion object {

        private const val server = "http://192.168.0.100:5049/"
        private const val recipesEndPoint = server + "Recipes/"
        private const val imagesEndPoint = recipesEndPoint + "img/"

        val recipes = mutableListOf<Recipe>()
        private val categories: HashMap<String, MutableList<Int>> = HashMap()

        fun fetchRecipes() {
            GlobalScope.launch(Dispatchers.IO) {
                val url = URL(recipesEndPoint)

                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"

                    // Optional: Set request headers
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Authorization", "Bearer YOUR_TOKEN")

                    val responseCode = responseCode
                    println("Response Code: $responseCode")

                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()
                        var inputLine = it.readLine()

                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }

                        val responseObjectList : List<ResponseObject> = Json.decodeFromString(response.toString())
                        recipes.clear()
                        categories.clear()

                        for ((index, obj) in responseObjectList.withIndex()) {
                            var description = ""
                            for (ingredient in obj.ingredients)
                                description += "- $ingredient\n"
                            description += '\n' + obj.description
                            recipes.add(Recipe(obj.category, obj.name, description, obj.steps, obj.image))

                            val category: MutableList<Int>? = categories[obj.category]
                            if (category == null) {
                                categories[obj.category] = mutableListOf(index)
                            }
                            else {
                                category.add(index)
                            }
                        }
                    }
                }
            }
        }

        fun getRecipes(category: String) : List<Recipe> {
            val categoryRecipes = mutableListOf<Recipe>()
            val indices: MutableList<Int>? = categories[category]
            if (indices != null) {
                for (index in indices) {
                    categoryRecipes.add(recipes[index])
                }
            }

            return categoryRecipes
        }

        fun positionToRecipeIndex(category: String, position: Int) : Int {
            val indices: MutableList<Int> = categories[category]!!
            return indices[position]
        }
    }

    fun getRecipe(): String {
        return this.recipe
    }

    fun getName(): String {
        return this.name
    }

    fun getSteps() : List<Long> {
        return this.steps
    }

    fun getImageURL() : String {
        return imagesEndPoint + this.image
    }

    override fun toString(): String {
        return this.name
    }
}