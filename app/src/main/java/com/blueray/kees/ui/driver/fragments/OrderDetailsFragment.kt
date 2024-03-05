package com.blueray.kees.ui.driver.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentOrderDetails2Binding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.DriverWeeklyBasketProduct
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.DriverHomeActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetails2Binding
    private val viewModel: AppViewModel by viewModels()
    private var basket_id: String? = null

    companion object {
        var productsList: List<DriverWeeklyBasketProduct>? = null
        var note: String? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetails2Binding.inflate(layoutInflater)
        prepareAppBar(getString(R.string.orderdetails))
        val orderId = arguments?.getString("orderId")
        val flag = arguments?.getString("fromWaiting")
        viewModel.retrieveDriverOrderDetails(orderId.toString())
        if (flag == "1"){
            binding.completeOrderButton.hide()
        }
        getData()
        getFinishedData()
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.viewProductsButton.setOnClickListener {
//            if (productsList != null) {
//                val bundle = bundleOf("productsList" to productsList)
//                findNavController().navigate(R.id.orderProductsFragment , bundle)
//            } else {
//                // Handle the case where productsList is null
//                Log.d("LOOSPP?" , ":")
//            }
//        }
        binding.completeOrderButton.setOnClickListener {
//            Toast.makeText(requireContext(), basket_id.toString(), Toast.LENGTH_LONG).show()
            viewModel.retrieveFinishOrder(basket_id.toString())

        }

    }

    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            (activity as DriverHomeActivity).onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

    private fun getData() {
        viewModel.getDriverOrderDetails().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        binding.numberOfProductsTv.text =
                            result.data.data.weekly_basket_products.count().toString()
                        binding.ClintNameTv.text = result.data.data.user_name
                        binding.ClintNumberTv.text = result.data.data.user_phone
                        binding.notesTv.text = result.data.data.note ?: ""
                        binding.dayTv.text =result.data.data.day
                        binding.dateTv.text = result.data.data.date
                        binding.deliveryTimeTv.text = result.data.data.start_time + " - " + result.data.data.end_time
                        // variables to pass to the product list page
                        productsList = result.data.data.weekly_basket_products
                        basket_id = result.data.data.id.toString()
                        note = result.data.data.note
                        binding.viewProductsButton.setOnClickListener {

                            findNavController().navigate(R.id.orderProductsFragment)
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
    private fun getFinishedData() {
        viewModel.getFinishOrder().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        val dialog = DoneDialogFragment()
                        dialog.show(childFragmentManager, "showDialog")
                        GlobalScope.launch {
                            delay(1000L)
                            val intent =Intent(requireContext(), DriverHomeActivity::class.java)
                            startActivity(intent)
                            activity?.finish()


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
