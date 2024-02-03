package com.blueray.kees.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.WeeksAdapter
import com.blueray.kees.databinding.FragmentAddToCartBottomSheetBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.model.WeeklyBasketData
import com.blueray.kees.ui.AppViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddToCartBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentAddToCartBottomSheetBinding
    private lateinit var adapter: WeeksAdapter
    private val viewModel: AppViewModel by viewModels()
    private var weeksList: List<WeeklyBasketData> = listOf()
    var productId: String? = null
    private lateinit var quantity: String
    private lateinit var colorId: String
    private lateinit var sizeId: String
    private lateinit var unitId: String
    private lateinit var weightId: String
    private lateinit var weeklyBasketIds: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddToCartBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        binding.addItem.setOnClickListener {
            binding.itemCount.text = (binding.itemCount.text.toString().toInt() + 1).toString()
        }
        binding.removeItem.setOnClickListener {
            if(binding.itemCount.text.toString().toInt()> 0){
                binding.itemCount.text = (binding.itemCount.text.toString().toInt() - 1).toString()
            }
        }

        // addToCart
        binding.addToCart.setOnClickListener {
            if (binding.itemCount.text.toString().toInt() > 0) {
                if (::weeklyBasketIds.isInitialized) {

                    quantity = binding.itemCount.text.toString()

                    viewModel.retrieveAddToBasket(
                        listOf(weeklyBasketIds.toInt()),
                        productId!!,
                        quantity,
                        colorId,
                        sizeId,
                        unitId,
                        weightId,
                        listOf()
                    )
                } else {
                    HelperUtils.showMessage(
                        requireContext(),
                        getString(R.string.PleaseSpecifyTheWeekToArrive)
                    )
                }

            } else {
                HelperUtils.showMessage(
                    requireContext(),
                    getString(R.string.SpecifyTheNuberOFItems)
                )
            }

        }
        // call Apis
        viewModel.retrieveProductDetails(productId!!)
        viewModel.retrieveWeeklyCart()


        // call Observers
        getWeeklyBasket()
        addToCart()
        getData()
    }

    private fun initAdapter() {
        adapter = WeeksAdapter(weeksList) { data, position ->
            weeksList[position].selected = !weeksList[position].selected
            weeksList.forEach {
                if (it != weeksList[position]) {
                    it.selected = false
                }
            }

            adapter.list = weeksList
            adapter.notifyDataSetChanged()
            weeklyBasketIds = data.id.toString()

        }
        val lm = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dayRv.adapter = adapter
        binding.dayRv.layoutManager = lm
    }

    private fun getData() {
        viewModel.getProductDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        val data = result.data.data
                        productId = data.id.toString()
                        colorId = data.color_id.toString()
                        sizeId = data.size_id.toString()
                        unitId = data.unit_id.toString()
                        weightId = data.weight_id.toString()
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
    private fun getWeeklyBasket() {
        viewModel.getWeeklyCart().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        weeksList = result.data.data
                        adapter.list = weeksList
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

    private fun addToCart() {
        viewModel.getAddToBasket().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(
                            requireContext(),
                            getString(R.string.addedToCartSuccessfully)
                        )
                        this.dismiss()
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