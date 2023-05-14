package com.example.recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECIPE_ID: String = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val frag: RecipeDetailFragment = supportFragmentManager
            .findFragmentById(R.id.detail_frag) as RecipeDetailFragment

        val recipeId: Int = intent.extras?.getInt(EXTRA_RECIPE_ID) ?: 0
        frag.setRecipe(recipeId.toLong())
    }
}