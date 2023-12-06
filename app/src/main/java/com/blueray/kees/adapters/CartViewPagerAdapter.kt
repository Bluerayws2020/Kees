package com.blueray.kees.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blueray.kees.model.WeeklyBasketProduct
import com.blueray.kees.ui.fragments.CartPageFragment
import com.blueray.kees.ui.fragments.ProductsFragment

class CartViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val productsList: ArrayList<ArrayList<WeeklyBasketProduct>> = ArrayList()
    private val weeksId: ArrayList<Int> = ArrayList()
    private val positionList: ArrayList<Int> = ArrayList()

    fun addCatList(categories: ArrayList<WeeklyBasketProduct>) {
        productsList.add(categories)
    }
    fun addWeekId(id:Int,po:Int){
        weeksId.add(id)
        positionList.add(po)
    }


    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun createFragment(position: Int): Fragment {
        val cartFragment = CartPageFragment()
        val bundle = Bundle()

        bundle.putParcelableArrayList("productsList", productsList[position])
        bundle.putInt("weekId", weeksId[position])
        bundle.putInt("Position", positionList[position])
        cartFragment.arguments = bundle
        return cartFragment
    }
}