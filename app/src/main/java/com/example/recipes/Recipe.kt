package com.example.recipes

class Recipe(name: String, recipe: String) {

    private val recipe = recipe;
    private val name = name;

    companion object {
        val recipes = arrayOf<Recipe> (
            Recipe("BloodyMary", "Składniki: \n 40 ml wódki \n 10 ml soku z cytryny \n 120 ml soku pomidorowego \n sos worchestershire\n sól \n pieprz \n tabasco \n gałązka selera naciowego \n\n Sposób przygotowania: \n Wszystkie składnikiwymieszać w szklance z lodem i ozdobić selerem naciowym."),
            Recipe("recipe1 title", "recipe1 description"),
            Recipe("recipe2 title", "recipe2 description"),
            Recipe("recipe3 title", "recipe3 description")
        )
    }

    fun getRecipe(): String {
        return this.recipe;
    }

    fun getName(): String {
        return this.name;
    }

    override fun toString(): String {
        return this.name;
    }
}