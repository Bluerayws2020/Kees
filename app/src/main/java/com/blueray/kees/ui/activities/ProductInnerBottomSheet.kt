package com.blueray.kees.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.ProductFeaturesAdapter
import com.blueray.kees.adapters.ProductVariationsAdapter
import com.blueray.kees.adapters.WeeksAdapter
import com.blueray.kees.databinding.FragmentProductInnerBottomSheetBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.model.WeeklyBasketData
import com.blueray.kees.ui.AppViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ProductInnerBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentProductInnerBottomSheetBinding
    private lateinit var adapter: WeeksAdapter
    private lateinit var variationsAdapter: ProductVariationsAdapter
    private lateinit var featureAdapter: ProductFeaturesAdapter
    private val viewModel: AppViewModel by viewModels()
    private var weeksList: List<WeeklyBasketData> = listOf()
    var productId: String? = null
    private lateinit var quantity: String
    private lateinit var colorId: String
    private lateinit var sizeId: String
    private lateinit var unitId: String
    private lateinit var weightId: String
    private lateinit var weeklyBasketIds: MutableList<Int>
    private var isWishlist: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProductInnerBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init adapter of days
        initAdapter()

        binding.addItem.setOnClickListener {
            binding.itemCount.text = (binding.itemCount.text.toString().toInt() + 1).toString()
        }
        binding.addBasketBtn.setOnClickListener {
            startActivity(Intent(requireContext(), AddNewBasketActivity::class.java))
        }
        binding.removeItem.setOnClickListener {
            if (binding.itemCount.text.toString().toInt() > 0) {
                binding.itemCount.text = (binding.itemCount.text.toString().toInt() - 1).toString()
            }
        }
        binding.favouriteButton.setOnClickListener {
            productId?.let { it1 -> viewModel.retrieveAddRemoveWishlistProduct(it1) }
            if (isWishlist == false) {
                binding.favouriteButton.setImageResource(R.drawable.heart)
                isWishlist = true
            } else {
                binding.favouriteButton.setImageResource(R.drawable.ic_heart)
                isWishlist = false
            }
//
        }

        // init features Adapter if existed
        initFeaturesAdapter()


        // addToCart
        binding.addToCart.setOnClickListener {
            if (binding.itemCount.text.toString().toInt() > 0) {
                if (::weeklyBasketIds.isInitialized) {

                    quantity = binding.itemCount.text.toString()
//                    weeklyBasketIds += weeklyBasketIds

                    viewModel.retrieveAddToBasket(
                        weeklyBasketIds,
                        productId!!,
                        quantity,
                        colorId,
                        sizeId,
                        unitId,
                        weightId,
                        featureAdapter.selectedItems
                    )
                } else {
                    showMessage(requireContext(), getString(R.string.PleaseSpecifyTheWeekToArrive))
                }

            } else {
                showMessage(requireContext(), getString(R.string.SpecifyTheNuberOFItems))
            }

        }
        // call Apis
        viewModel.retrieveProductDetails(productId!!)
        viewModel.retrieveWeeklyCart()


        // call Observers
        getData()
        getWeeklyBasket()
        addToCart()
        addToWishList()
    }

    private fun initFeaturesAdapter() {
        featureAdapter = ProductFeaturesAdapter(listOf())
        val lm = GridLayoutManager(requireContext(), 2)
        binding.featuresRv.layoutManager = lm
        binding.featuresRv.adapter = featureAdapter
    }


    private fun initAdapter() {
        adapter = WeeksAdapter(weeksList) { data, position ->
            // Toggle the selected state of the clicked week
            weeklyBasketIds = mutableListOf()
            weeksList[position].selected = !weeksList[position].selected

            // Clear the list of selected weekly basket IDs
            weeklyBasketIds.clear()

            // Iterate through the weeks list
            weeksList.forEach { week ->
                // Check if the week is selected
                if (week.selected) {
                    // If selected, add its ID to the list of selected weekly basket IDs
                    weeklyBasketIds.add(week.id)
                }
            }

            // Notify the adapter of the data change
            adapter.notifyDataSetChanged()

            // Log the selected weekly basket IDs
            Log.d("WeEEEEWS", weeklyBasketIds.toString())
        }

        val lm = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.daysRv.adapter = adapter
        binding.daysRv.layoutManager = lm
    }

    private fun getData() {
        viewModel.getProductDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        val data = result.data.data
                        Glide.with(requireContext()).load(data.image).placeholder(R.drawable.tahini)
                            .into(binding.productImage)
                        binding.productName.text = data.name
                        if (result.data.data.variation_type == "Standard") {
                            if (data.on_sale_price_status == "Active"){
                                binding.price.text = data.on_sale_price
                            }else{
                                binding.price.text = data.sale_price
                            }
                            colorId = data.color_id.toString()
                            sizeId = data.size_id.toString()
                            unitId = data.unit_id.toString()
                            weightId = data.weight_id.toString()
                        } else {
                            binding.price.text = ""
                            binding.variationsRv.show()
                            variationsAdapter =
                                ProductVariationsAdapter(result.data.data.variations_multiple) { color_id, size_id, weight_id, unit_id ->
                                    colorId = color_id.toString()
                                    sizeId = size_id.toString()
                                    unitId = unit_id.toString()
                                    weightId = weight_id.toString()
                                }
                            binding.variationsRv.adapter = variationsAdapter
                            binding.variationsRv.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                        binding.productDescription.text = data.description
                        productId = data.id.toString()

                        if (data.is_wishlist == true) {
                            binding.favouriteButton.setImageResource(R.drawable.heart)
                            isWishlist = true
                        } else {
                            binding.favouriteButton.setImageResource(R.drawable.ic_heart)
                            isWishlist = false
                        }
                        if (!data.features.isNullOrEmpty()) {
                            binding.featuresRv.show()
                            featureAdapter.list = data.features
                            featureAdapter.notifyDataSetChanged()
                        } else {
                            binding.featuresRv.hide()
                        }

                    } else {
                        showMessage(requireContext(), result.data.message)
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    showMessage(
                        requireContext(),
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }

    private fun getWeeklyBasket() {
        viewModel.getWeeklyCart().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        weeksList = result.data.data
                        adapter.list = weeksList
                        adapter.notifyDataSetChanged()

                    } else {
                        showMessage(requireContext(), result.data.message)
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    showMessage(
                        requireContext(),
                         getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }

    private fun addToCart() {
        viewModel.getAddToBasket().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        showMessage(requireContext(), getString(R.string.addedToCartSuccessfully))
                        this.dismiss()
                    } else {
                        showMessage(requireContext(), result.data.message)
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    showMessage(
                        requireContext(),
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }

    private fun addToWishList() {
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
                        HelperUtils.showMessage(requireContext(), result.data.error)
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