package com.example.recipes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity(), RecipeListFragment.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun itemClicked(id: Long) {
        val intent = Intent(this, DetailActivity::class.java);
        intent.putExtra(DetailActivity.EXTRA_RECIPE_ID, id.toInt())
        startActivity(intent);
    }
}