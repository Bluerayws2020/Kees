package com.blueray.kees.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.ProductsAdapter
import com.blueray.kees.databinding.FragmentProductsBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.ProductInnerBottomSheet


class ProductsFragment : Fragment() {

    private lateinit var binding : FragmentProductsBinding
    private lateinit var adapter : ProductsAdapter
    private val viewModel : AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProductsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        val catId = arguments?.getString("category")

        // call API
        viewModel.retrieveProducts(
            categoryId = catId
        )
        // observe to live data
        getProducts()
        addRemoveFromWishList()

    }

    private fun setAdapter() {
        adapter = ProductsAdapter(listOf(),{},{})
        adapter.onClickListener =
        {
            val productDetails = ProductInnerBottomSheet()
            productDetails.productId = it
            productDetails.show(parentFragmentManager,"bottomSheet")
        }
        adapter.onLikeClickListener = {
            // call API
            viewModel.retrieveAddRemoveWishlistProduct(it)
        }
        adapter.addToCartClickListener {
            val addToCart = AddToCartBottomSheetFragment()
            addToCart.productId = it
            addToCart.show(parentFragmentManager,"bottomShow")
        }
        val lm  = LinearLayoutManager(requireContext())
        binding.productsRv.layoutManager = lm
        binding.productsRv.adapter = adapter
    }

    private fun getProducts(){
        viewModel.getProducts().observe(viewLifecycleOwner){
            result ->
            when(result){
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter.list = result.data.data
                        adapter.notifyDataSetChanged()
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