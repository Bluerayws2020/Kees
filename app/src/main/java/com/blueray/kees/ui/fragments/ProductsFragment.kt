package com.blueray.kees.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.ProductsAdapter
import com.blueray.kees.databinding.FragmentProductsBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.ProductInnerBottomSheet


class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    private lateinit var adapter: ProductsAdapter
    private val viewModel: AppViewModel by viewModels()
    private lateinit var catId: String
    private var subId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        // Retrieve category and subcategory IDs from arguments bundle
        catId = arguments?.getString("category") ?: ""
        val initialSubId = arguments?.getString("subCategory")
        if (initialSubId != null) {
            subId = initialSubId
        }
        Log.d("FragmentIds", "Category: $catId, SubCategory: $subId")
        // Call API with retrieved IDs
        viewModel.retrieveProducts(categoryId = catId, subCategoryId = subId)
                binding.search.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.retrieveProducts(
                    textSearch = binding.search.text.toString(),
                    categoryId = catId, subCategoryId = subId
                )
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        // Observe LiveData
        getProducts()
        addRemoveFromWishList()
    }

    private fun setAdapter() {
        adapter = ProductsAdapter(listOf(), {}, {}, null,false)
        adapter.onClickListener = { productId ->
                val addToCart = ProductInnerBottomSheet()
                addToCart.productId = productId
                addToCart.show(parentFragmentManager, "bottomShow")

        }
        adapter.onLikeClickListener = { productId ->
            viewModel.retrieveAddRemoveWishlistProduct(productId)
        }
        adapter.onClickListener =
            {
                val productDetails = ProductInnerBottomSheet()
                productDetails.productId = it
                productDetails.show(parentFragmentManager, "bottomSheet")
            }
        val lm = LinearLayoutManager(requireContext())
        binding.productsRv.layoutManager = lm
        binding.productsRv.adapter = adapter
    }

    private fun getProducts() {
        viewModel.getProducts().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter.list = result.data.data ?: listOf()
                        adapter.notifyDataSetChanged()
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        requireContext(),
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }

    private fun addRemoveFromWishList() {
        viewModel.getAddRemoveWishlistProduct().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        if (result.data.data.is_wishlist == true){
                            HelperUtils.showMessage(requireContext(), "Added Successfully")
                        }else{
                            HelperUtils.showMessage(requireContext(), "Removed Successfully")
                        }
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        requireContext(),
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }
}
