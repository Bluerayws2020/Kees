package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.blueray.kees.R
import com.blueray.kees.adapters.CartViewPagerAdapter
import com.blueray.kees.databinding.ActivityCartBinding
import com.blueray.kees.databinding.DayItemBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.model.WeeklyBasketData
import com.blueray.kees.model.WeeklyBasketProduct
import com.blueray.kees.ui.AppViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CartActivity : BaseActivity() {

    private lateinit var binding: ActivityCartBinding
    private val viewModel: AppViewModel by viewModels()
    private var weeksList = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressed()
        }
        binding.includedTap.back.setImageResource(R.drawable.add_ic)
        binding.includedTap.title.text = getString(R.string.weeklyCart)
        binding.includedTap.back.setOnClickListener {
            val apiResponseWeeks = weeksList // Assuming the API response returns these weeks
            val missingWeeks = filterMissingWeeks(apiResponseWeeks)
            println("Missing weeks: $missingWeeks")
            startActivity(Intent(this, AddNewBasketActivity::class.java))
        }


        // observe to Live Data
        getCarts()

        // Call Api
        viewModel.retrieveWeeklyCart()

        binding.continueToPaymentBtn.setOnClickListener {
            startActivity(Intent(this, CheckOutActivity::class.java))
        }

    }

    private fun setUpViewPagerWithTapLayout(list: List<WeeklyBasketData>) {
        val adapter = CartViewPagerAdapter(supportFragmentManager, lifecycle)
        val tabListTitle: MutableList<String> = ArrayList()

        for (i in list.indices) {
            val item = list[i]
            if (item.week_number == "First") {
                weeksList.add(1)
            }
            if (item.week_number == "Second") {
                weeksList.add(2)
            }
            // send products to the fragments
            val productsArray = arrayListOf<WeeklyBasketProduct>()
            item.weekly_basket_products.forEach {
                productsArray.add(it)
            }
            adapter.addCatList(productsArray)
            adapter.addWeekId(item.id, i)

            // send category name to the tab layout
            tabListTitle.add(item.week_number + getString(R.string.week))
        }

        binding.viewPager.adapter = adapter

        for (item in tabListTitle) {
            binding.weeksTabLayout.addTab(binding.weeksTabLayout.newTab())
        }

        TabLayoutMediator(
            binding.weeksTabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            val tabBinding = DayItemBinding.inflate(layoutInflater)
            tabBinding.dayText.text = tabListTitle[position]
            tab.setCustomView(tabBinding.root)
            if (position == 0) {
                tab.view.findViewById<CardView>(R.id.card).setCardBackgroundColor(
                    ContextCompat.getColor(
                        this@CartActivity,
                        R.color.purpleColor
                    )
                )
                tab.view.findViewById<TextView>(R.id.dayText)
                    .setTextColor(ContextCompat.getColor(this@CartActivity, R.color.white))
            }
        }.attach()

        binding.weeksTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab!!.view.findViewById<CardView>(R.id.card).setCardBackgroundColor(
                    ContextCompat.getColor(
                        this@CartActivity,
                        R.color.purpleColor
                    )
                )
                tab.view.findViewById<TextView>(R.id.dayText)
                    .setTextColor(ContextCompat.getColor(this@CartActivity, R.color.white))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab!!.view.findViewById<CardView>(R.id.card).setCardBackgroundColor(
                    ContextCompat.getColor(
                        this@CartActivity,
                        R.color.lightGrey
                    )
                )
                tab.view.findViewById<TextView>(R.id.dayText)
                    .setTextColor(ContextCompat.getColor(this@CartActivity, R.color.dark_grey))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab!!.view.findViewById<CardView>(R.id.card).setCardBackgroundColor(
                    ContextCompat.getColor(
                        this@CartActivity,
                        R.color.purpleColor
                    )
                )
                tab.view.findViewById<TextView>(R.id.dayText)
                    .setTextColor(ContextCompat.getColor(this@CartActivity, R.color.white))
            }

        })
    }

    private fun getCarts() {
        viewModel.getWeeklyCart().observe(this) { result ->
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

    fun filterMissingWeeks(selectedWeeks: List<Int>, maxWeeks: Int = 4): List<Int> {
        val allWeeks = (1..maxWeeks).toList()
        val missingWeeks = allWeeks.filter { !selectedWeeks.contains(it) }
        return missingWeeks
    }
}