package com.example.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.getColor
class TopFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_top, container, false)
        val logoView = view.findViewById<ImageView>(R.id.logo)
        logoView.setColorFilter(getColor(view.context, R.color.purple_200))
        val drawable = resources.getDrawable(R.drawable.logo)
        logoView.setImageDrawable(drawable)
        return view
    }
}