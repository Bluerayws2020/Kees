package com.blueray.kees.ui.activities

import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.blueray.kees.R
import com.blueray.kees.adapters.CategoryPagerAdapter
import com.blueray.kees.databinding.ActivityProductsBinding
import com.blueray.kees.databinding.DayItemBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.GetMainCategoriesData
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.google.android.material.internal.ViewUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductsBinding

    private lateinit var catId: String
    private var subId: String = "" // Initialize subId with an empty string
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = getString(R.string.all_categories)

        catId = intent.getStringExtra("cat_id") ?: ""

        // call Api
        viewModel.retrieveSubCategories(catId)
        // observe to live data
        getSubCategoryData()

    }

    private fun setUpViewPagerWithTapLayout(list: List<GetMainCategoriesData>, subIds: List<String>) {
        val adapter = CategoryPagerAdapter(supportFragmentManager, lifecycle, list.map { it.id.toString() }, subIds)
        val tabListTitle: MutableList<String> = ArrayList()
        list.forEach {
            // send category name to the tab layout
            tabListTitle.add(it.name)
        }
        binding.viewPager.adapter = adapter

        for (item in tabListTitle) {
            // Set the custom view for the tab
            val tab = binding.tabLayout.newTab()
            binding.tabLayout.addTab(tab)
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            val tabBinding = DayItemBinding.inflate(layoutInflater)
            tabBinding.dayText.text = tabListTitle[position]
            tab.setCustomView(tabBinding.root)
            if (position == 0) {
                tab.view.findViewById<CardView>(R.id.card).setCardBackgroundColor(ContextCompat.getColor(this@ProductsActivity, R.color.purpleColor))
                tab.view.findViewById<TextView>(R.id.dayText).setTextColor(ContextCompat.getColor(this@ProductsActivity, R.color.white))
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab!!.view.findViewById<CardView>(R.id.card).setCardBackgroundColor(ContextCompat.getColor(this@ProductsActivity, R.color.purpleColor))
                tab.view.findViewById<TextView>(R.id.dayText).setTextColor(ContextCompat.getColor(this@ProductsActivity, R.color.white))

                // Update subId when a new tab is selected
                subId = subIds.getOrNull(tab.position) ?: ""
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab!!.view.findViewById<CardView>(R.id.card).setCardBackgroundColor(ContextCompat.getColor(this@ProductsActivity, R.color.lightGrey))
                tab.view.findViewById<TextView>(R.id.dayText).setTextColor(ContextCompat.getColor(this@ProductsActivity, R.color.dark_grey))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab!!.view.findViewById<CardView>(R.id.card).setCardBackgroundColor(ContextCompat.getColor(this@ProductsActivity, R.color.purpleColor))
                tab.view.findViewById<TextView>(R.id.dayText).setTextColor(ContextCompat.getColor(this@ProductsActivity, R.color.white))
            }

        })
    }

    private fun getSubCategoryData() {
        viewModel.getSubCategories().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        val subIds = result.data.data.map { it.id.toString() } // Extract subIds from result
                        subId = subIds.firstOrNull() ?: ""
                        Log.d("SubIds", subIds.toString())// Assign the first subId to subId
                        setUpViewPagerWithTapLayout(result.data.data, subIds)
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }
                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(this, result.data?.message ?: getString(R.string.Error))
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }
}


