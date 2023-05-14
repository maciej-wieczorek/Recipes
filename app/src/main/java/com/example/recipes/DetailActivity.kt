package com.example.recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

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
        val img: ImageView = findViewById(R.id.collapsing_image)
        val drawable = ContextCompat.
            getDrawable(this, Recipe.recipes[recipeId].getImageResourceId())
        img.setImageDrawable(drawable)
    }
}