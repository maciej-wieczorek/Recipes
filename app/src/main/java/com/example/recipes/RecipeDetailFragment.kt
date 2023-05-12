package com.example.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecipeDetailFragment : Fragment() {

    private var recipeId: Long = 0
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getLong("recipeId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        val recipe: Recipe = Recipe.recipes[recipeId.toInt()]
        val title: TextView = rootView.findViewById<TextView>(R.id.textTitle)
        title.text = recipe.getName()
        val description: TextView = rootView.findViewById<TextView>(R.id.textDescription)
        description.text = recipe.getRecipe()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("recipeId", recipeId)
    }

    fun setRecipe(id: Long) {
        recipeId = id
    }
}