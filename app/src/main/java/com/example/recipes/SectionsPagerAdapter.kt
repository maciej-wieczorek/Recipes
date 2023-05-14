package com.example.recipes

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(fm: FragmentManager?, private val context: Context) : FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            1 -> return Tab1Fragment()
            2 -> return Tab2Fragment()
        }
        return TopFragment()
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            1 -> return context.getString(R.string.cat1_tab)
            2 -> return context.getString(R.string.cat2_tab)
        }
        return context.getString(R.string.home_tab)
    }
}
