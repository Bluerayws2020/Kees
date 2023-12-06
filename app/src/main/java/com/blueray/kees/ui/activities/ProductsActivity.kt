package com.blueray.kees.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.adapters.CategoryPagerAdapter
import com.blueray.kees.databinding.ActivityProductsBinding
import com.blueray.kees.databinding.FragmentAllCategroiesBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.GetMainCategoriesData
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductsBinding
    private lateinit var id: String
    private val viewModel: AppViewModel by viewModels()
//    private var categoriesList = mutableListOf<GetMainCategoriesData>()

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

        id = intent.getStringExtra("cat_id") ?: ""

        // call Api
        viewModel.retrieveSubCategories(id)
        // observe to live data
        getSubCategoryData()



    }

    private fun setUpViewPagerWithTapLayout(list: List<GetMainCategoriesData>) {
        val adapter = CategoryPagerAdapter(supportFragmentManager, lifecycle)
        val tabListTitle: MutableList<String> = ArrayList()
        list.forEach {
            // send category id to the fragments
            adapter.addCatList(it.id.toString())
            // send category name to the tab layout
            tabListTitle.add(it.name)
        }
        binding.viewPager.adapter = adapter

        for (item in tabListTitle) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(item))
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabListTitle[position]
        }.attach()

    }

    private fun getSubCategoryData() {
        viewModel.getSubCategories().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        setUpViewPagerWithTapLayout(result.data.data)
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