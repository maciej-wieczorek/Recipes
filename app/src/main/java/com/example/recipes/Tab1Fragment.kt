package com.example.recipes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Tab1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recipeRecycler: RecyclerView =
            inflater.inflate(R.layout.fragment_tab1, container, false) as RecyclerView

        val adapter = CaptionedImagesAdapter(Recipe.recipes)
        recipeRecycler.adapter = adapter

        val layoutManager = GridLayoutManager(activity, 2)
        recipeRecycler.layoutManager = layoutManager

        adapter.setListener(object : CaptionedImagesAdapter.Listener {
            override fun onClick(position: Int) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_RECIPE_ID, position)
                activity?.startActivity(intent)
            }
        })

        return recipeRecycler
    }
}