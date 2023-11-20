package com.blueray.kees.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.AllCategoriesAdapter
import com.blueray.kees.adapters.HomeCategoryAdapter
import com.blueray.kees.databinding.FragmentAllCategroiesBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.GetMainCategoriesData
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.ProductsActivity
import kotlinx.coroutines.Job


class AllCategoriesFragment : Fragment() {

    private lateinit var binding : FragmentAllCategroiesBinding
    private lateinit var categoryAdapter :AllCategoriesAdapter
    private val viewModel : AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllCategroiesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retrieveMainCategories()
        getData()
    }

    private fun initCategoriesItem(list: List<GetMainCategoriesData>) {
        categoryAdapter = AllCategoriesAdapter(list){
            startActivity(Intent(requireActivity(),ProductsActivity::class.java).apply {
                putExtra("cat_id",it)
            })
        }
        val lm = GridLayoutManager(requireContext(),3)
        binding.categoriesRv.layoutManager = lm
        binding.categoriesRv.adapter = categoryAdapter
    }
    private fun getData(){
        viewModel.getMainCategories().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        // init adapters
                        initCategoriesItem(result.data.data)
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                    }
                }
                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(requireContext(), result.data?.message?: getString(R.string.Error))
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }

}