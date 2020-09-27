package com.masrooraijaz.fireinsta.ui.main.home.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {
                SearchAccountFragment()
            }
            1 -> {
                SearchPostsFragment()
            }
            else -> throw Exception("No fragment")
        }
    }
}