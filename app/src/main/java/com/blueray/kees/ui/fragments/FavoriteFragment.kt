package com.blueray.kees.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.ProductsAdapter
import com.blueray.kees.databinding.FragmentFavoriteFargmentBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.HomeActivity
import com.blueray.kees.ui.activities.ProductInnerBottomSheet

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteFargmentBinding
    private lateinit var adapter: ProductsAdapter
    private val viewModel: AppViewModel by viewModels()
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteFargmentBinding.inflate(layoutInflater)
        binding.includedTap.title.text = getString(R.string.favorite)
        binding.includedTap.back.hide()
        binding.includedTap.menu.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe To LiveData
        setAdapter()
        addRemoveFromWishList()
        getProducts()

        // get Favorite products
        viewModel.retrieveFavoriteProducts()

        // handle bottom nav animation
        binding.productsRv.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
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

    private fun setAdapter() {
        adapter = ProductsAdapter(listOf(), {}, {})
        adapter.onClickListener =
            {
                val productDetails = ProductInnerBottomSheet()
                productDetails.productId = it
                productDetails.show(parentFragmentManager, "bottomSheet")
            }

        adapter.onLikeClickListener = {
            // call API
            viewModel.retrieveAddRemoveWishlistProduct(it)
        }
        adapter.addToCartClickListener {
            val addToCart = AddToCartBottomSheetFragment()
            addToCart.productId = it
            addToCart.show(parentFragmentManager, "bottomShow")
        }
        val lm = LinearLayoutManager(requireContext())
        binding.productsRv.layoutManager = lm
        binding.productsRv.adapter = adapter
    }

    private fun addRemoveFromWishList() {
        viewModel.getAddRemoveWishlistProduct().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(requireContext(), result.data.message)
                        viewModel.retrieveFavoriteProducts()
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

    private fun getProducts() {
        viewModel.getFavoriteProducts().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter.list = result.data.data
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
}