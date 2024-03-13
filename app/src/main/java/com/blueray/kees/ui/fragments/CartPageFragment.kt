package com.blueray.kees.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.CartItemAdapter
import com.blueray.kees.databinding.FragmentCartPageBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.model.WeeklyBasketProduct
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.driver.fragments.DoneDialogFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class CartPageFragment : Fragment() {
    private lateinit var binding: FragmentCartPageBinding
    private lateinit var adapter : CartItemAdapter
    private lateinit var productsList: ArrayList<WeeklyBasketProduct>
    private lateinit var weekId: List<Int>
    private var positionOfPager by Delegates.notNull<Int>()
    private val viewModel : AppViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartPageBinding.inflate(layoutInflater)
        binding.addNoteButton.setOnClickListener {
            openNotesPopUp()
        }
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsList = arguments!!.getParcelableArrayList("productsList")!!
        weekId = listOf(arguments!!.getInt("weekId"))
        positionOfPager = arguments!!.getInt("Position")

        // set up recycler view
        setUpRecyclerView()

        // observe to observers
        addToCart()
        getCarts()
    }

    private fun setUpRecyclerView() {
        adapter = CartItemAdapter(onPlusClick)
        adapter.differ.submitList(productsList)
        val lm = LinearLayoutManager(requireContext())
        binding.productsRv.adapter = adapter
        binding.productsRv.layoutManager =lm

    }
    private var plusJob : Job? = null
    private val onPlusClick : (productId: Int, quantity: String, colorId: Int, sizeId: Int, unitId: Int, weight: Int, position: Int,itemCount:Int)->Unit = {
        productId,quantity,colorId,sizeId,unitId ,weight,position,itemCount ->
//        plusJob?.cancel()
        plusJob = MainScope().launch {
            delay(50L)
            val addedQuantity = itemCount
            viewModel.retrieveAddToBasket(weekId,productId.toString(),addedQuantity.toString(),colorId.toString(),sizeId.toString(),unitId.toString(),weight.toString())
        }
    }


    private fun addToCart(){
        viewModel.getAddToBasket().observe(viewLifecycleOwner){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        viewModel.retrieveWeeklyCart()
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

    private fun getCarts(){
        viewModel.getWeeklyCart().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        val newProductList = arrayListOf<WeeklyBasketProduct>()
                        result.data.data[positionOfPager].weekly_basket_products.forEach {
                            newProductList.add(it)
                        }
                        productsList = newProductList
                        adapter.differ.submitList(productsList)
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
    private fun openNotesPopUp() {
        val dialog = NotesDialogFragment()
        dialog.show(childFragmentManager, "showDialog")
    }

}
