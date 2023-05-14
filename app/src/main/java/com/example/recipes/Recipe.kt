package com.example.recipes

class Recipe(name: String, recipe: String, time: Long, imageId: Int) {

    private val recipe = recipe
    private val name = name
    private val time = time
    private val imageResourceId = imageId

    companion object {
        val recipes = arrayOf<Recipe> (
            Recipe("BloodyMary", "Składniki: \n 40 ml wódki \n 10 ml soku z cytryny \n 120 ml soku pomidorowego \n sos worchestershire\n sól \n pieprz \n tabasco \n gałązka selera naciowego \n\n Sposób przygotowania: \n Wszystkie składnikiwymieszać w szklance z lodem i ozdobić selerem naciowym.", 100, R.drawable.r1),
            Recipe("recipe1 title", "recipe1 description \na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na", 50, R.drawable.r2),
            Recipe("recipe2 title", "recipe2 description", 101, R.drawable.r3),
            Recipe("recipe3 title", "recipe3 description", 105, R.drawable.r4),
            Recipe("recipe4 title", "recipe4 description", 50, R.drawable.r2),
            Recipe("recipe5 title", "recipe5 description", 101, R.drawable.r3),
            Recipe("recipe6 title", "recipe6 description", 105, R.drawable.r4),
            Recipe("recipe7 title", "recipe7 description", 50, R.drawable.r2),
            Recipe("recipe8 title", "recipe8 description", 101, R.drawable.r3),
            Recipe("recipe9 title", "recipe9 description", 105, R.drawable.r4),
            Recipe("recipe10 title", "recipe10 description", 50, R.drawable.r2),
            Recipe("recipe11 title", "recipe11 description", 101, R.drawable.r3),
            Recipe("recipe12 title", "recipe12 description", 105, R.drawable.r4)
        )
    }

    fun getRecipe(): String {
        return this.recipe
    }

    fun getName(): String {
        return this.name
    }

    fun getTime() : Long {
        return this.time
    }

    fun getImageResourceId() : Int {
        return this.imageResourceId
    }

    override fun toString(): String {
        return this.name
    }
}