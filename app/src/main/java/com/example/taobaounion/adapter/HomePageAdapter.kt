package com.example.taobaounion.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taobaounion.data.home.Category
import com.example.taobaounion.ui.home.HomePageFragment

class HomePageAdapter(private val categoryList:List<Category>,fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        val fragment=HomePageFragment()
        fragment.arguments=Bundle().apply {
            putInt("categoryId",categoryList[position].id)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}