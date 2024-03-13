package com.blueray.kees.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blueray.kees.ui.fragments.ProductsFragment

class CategoryPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val categoriesList: List<String>,
    private val subCategoriesList: List<String>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

//    private val categoriesList: ArrayList<String> = ArrayList()
//    private val subCategoriesList: ArrayList<String> = ArrayList()



    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ProductsFragment()
        val bundle = Bundle().apply {
//            putString("category", categoriesList[position])
            putString("subCategory", subCategoriesList[position])
        }
        fragment.arguments = bundle
        return fragment
    }

}