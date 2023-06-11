package com.example.recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso

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
        val imgView: ImageView = findViewById(R.id.collapsing_image)
        Picasso.get().load(Recipe.recipes[recipeId].getImageURL()).into(imgView)
        title = Recipe.recipes[recipeId].getName()
    }
}