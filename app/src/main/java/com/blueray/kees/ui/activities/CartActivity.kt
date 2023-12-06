package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.adapters.CartViewPagerAdapter
import com.blueray.kees.databinding.ActivityCartBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.model.WeeklyBasketData
import com.blueray.kees.model.WeeklyBasketProduct
import com.blueray.kees.ui.AppViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CartActivity : BaseActivity() {

    private lateinit var binding : ActivityCartBinding
    private val viewModel : AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener { 
            onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = getString(R.string.weeklyCart)

        // observe to Live Data
        getCarts()

        // Call Api
        viewModel.retrieveWeeklyCart()
        
        binding.continueToPaymentBtn.setOnClickListener { 
            startActivity(Intent(this,CheckOutActivity::class.java))
        }

    }
    private fun setUpViewPagerWithTapLayout(list: List<WeeklyBasketData>) {
        val adapter = CartViewPagerAdapter(supportFragmentManager, lifecycle)
        val tabListTitle: MutableList<String> = ArrayList()

        for (i in list.indices) {
            val item = list[i]
            // send products to the fragments
            val productsArray = arrayListOf<WeeklyBasketProduct>()
            item.weekly_basket_products.forEach {
                productsArray.add(it)
            }
            adapter.addCatList(productsArray)
            adapter.addWeekId(item.id,i)

            // send category name to the tab layout
            tabListTitle.add(item.week_number + getString(R.string.week))
        }

        binding.viewPager.adapter = adapter

        for (item in tabListTitle) {
            binding.weeksTabLayout.addTab(binding.weeksTabLayout.newTab().setText(item))
        }

        TabLayoutMediator(
            binding.weeksTabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabListTitle[position]

        }.attach()
    }

    private fun getCarts(){
        viewModel.getWeeklyCart().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {

                        setUpViewPagerWithTapLayout(result.data.data)
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }
                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        this,
                        result.data?.message ?: getString(R.string.Error)
                    )
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }
}