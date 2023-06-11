package com.example.recipes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<Long>,
    val image: String
)

class Recipe(name: String, recipe: String, steps: List<Long>, image: String) {

    private val name = name
    private val recipe = recipe
    private val steps = steps
    private val image = image

    companion object {

        val server = "http://192.168.0.100:5049/"
        val recipesEndPoint = server + "Recipes/"
        val imagesEndPoint = recipesEndPoint + "img/"

        val recipes = mutableListOf<Recipe>()

        fun getRecipes() {
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

                        for (obj in responseObjectList) {
                            var description = ""
                            for (ingredient in obj.ingredients)
                                description += "- $ingredient\n"
                            description += '\n' + obj.description
                            recipes.add(Recipe(obj.name, description, obj.steps, obj.image))
                        }
                    }
                }
            }
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