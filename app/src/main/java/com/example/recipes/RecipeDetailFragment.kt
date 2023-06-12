package com.example.recipes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class RecipeDetailFragment : Fragment(), View.OnClickListener {

    private var recipeId: Long = 0
    private lateinit var rootView: View
    private lateinit var timer: TimerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timer = TimerFragment()
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getLong("recipeId")
        }
        else {
            val ft = childFragmentManager.beginTransaction()
            ft.add(R.id.timer_container, timer)
            ft.addToBackStack(null)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        val fab: View = rootView.findViewById(R.id.fab)
        fab.setOnClickListener(this)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val recipe: Recipe = Recipe.recipes[recipeId.toInt()]
        val description: TextView = rootView.findViewById<TextView>(R.id.textDescription)
        timer.setDuration(Recipe.recipes[recipeId.toInt()].getSteps()[0])
        description.text = recipe.getRecipe()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("recipeId", recipeId)
    }

    fun setRecipe(id: Long) {
        recipeId = id
    }

    private fun onClickFAB() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = Recipe.recipes[recipeId.toInt()].getRecipe()
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> onClickFAB()
        }
    }
}