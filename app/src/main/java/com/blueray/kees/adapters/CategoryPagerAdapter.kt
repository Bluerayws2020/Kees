package com.blueray.kees.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blueray.kees.ui.fragments.ProductsFragment

class CategoryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val categoriesList: ArrayList<String> = ArrayList()

    fun addCatList(categories: String) {
        categoriesList.add(categories)
    }


    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun createFragment(position: Int): Fragment {
        val productFragment = ProductsFragment()
        val bundle = Bundle()

        bundle.putString("category", categoriesList[position])
        productFragment.arguments = bundle
        return productFragment
    }
}