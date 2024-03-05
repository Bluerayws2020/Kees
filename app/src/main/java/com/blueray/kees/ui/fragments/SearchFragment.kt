package com.blueray.kees.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.blueray.kees.databinding.FragmentSearchBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.HomeActivity
import com.blueray.kees.ui.activities.ProductInnerBottomSheet
import com.google.android.material.internal.ViewUtils.hideKeyboard


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var productAdapter: ProductsAdapter
    private val viewModel: AppViewModel by viewModels()
    override fun onResume() {
        super.onResume()
        (requireContext() as HomeActivity).setSelectedId(R.id.searchFragment)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        binding.includedTap.title.text = getString(R.string.search)
        binding.includedTap.back.hide()
        binding.includedTap.menu.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
        return binding.root

    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        // Observe to live data
        getData()
        addRemoveFromWishList()

        // on keyboard search
        binding.search.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard(requireView())
                viewModel.retrieveProducts(
                    textSearch = binding.search.text.toString()
                )
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.productsSearchRecycler.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                // Scrolling down and not at the top
                if((activity as HomeActivity).getIsBottomShowing()){
                    (activity as HomeActivity).animateHideBottomNav()
                }
            } else {
                // Scrolling up or at the top
                if(!(activity as HomeActivity).getIsBottomShowing()){
                    (activity as HomeActivity).animateShowBottomNav()
                }
            }
        }


    }

    private fun setUpRecyclerView() {
        productAdapter = ProductsAdapter(listOf(),{},{} , false)
        productAdapter.onClickListener = {
            val productDetails = ProductInnerBottomSheet()
            productDetails.productId = it
            productDetails.show(parentFragmentManager, "bottomSheet")
        }
        productAdapter.onLikeClickListener = {
            // Call API
            viewModel.retrieveAddRemoveWishlistProduct(productId = it)
        }
        productAdapter.addToCartClickListener {
            val addToCart = ProductInnerBottomSheet()
            addToCart.productId = it
            addToCart.show(parentFragmentManager, "bottomShow")
        }
        val lm = LinearLayoutManager(requireContext())
        binding.productsSearchRecycler.adapter = productAdapter
        binding.productsSearchRecycler.layoutManager = lm
    }

    private fun getData() {
        viewModel.getProducts().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        productAdapter.list = result.data.data
                        productAdapter.notifyDataSetChanged()
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
    private fun addRemoveFromWishList(){
        viewModel.getAddRemoveWishlistProduct().observe(viewLifecycleOwner){
                result ->
            when(result){
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(requireContext(), result.data.message)
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                    }
                }
                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(requireContext(), result.data?.message ?: getString(R.string.Error))
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }
}
