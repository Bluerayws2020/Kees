package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.CheckOutWeekAdapter
import com.blueray.kees.api.OnLocationSelectedListener
import com.blueray.kees.databinding.ActivityCheckOutFragmentBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.CustomerGetAddressesData
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.fragments.ChoseLocationDialog
import com.blueray.kees.ui.fragments.NotesDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckOutActivity : BaseActivity(), OnLocationSelectedListener {
    companion object {
        var position = 0
    }

    private lateinit var binding: ActivityCheckOutFragmentBinding
    private val viewModel: AppViewModel by viewModels()
    private lateinit var adapter: CheckOutWeekAdapter
    var basketId = ""
    var customerAddressId = ""
    private lateinit var addressesList: List<CustomerGetAddressesData>
    var pos = ChoseLocationDialog.pos ?: 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = getString(R.string.checkOut)
//        HelperUtils.showMessage(this , NotesDialogFragment.note ?: "wew")
        // init Adapter
        setUpRecyclerView()
        viewModel.retrieveCustomerAddresses()
        binding.checkOutButton.setOnClickListener {
            viewModel.retrieveCheckOutSingleBasket(
                basketId,
                "",
                customerAddressId,
                "",
                "",
                "",
                "5",
                "",
                "",
                NotesDialogFragment.note ?: ""
            )
//           HelperUtils.showMessage(this, basketId.toString())
        }
        binding.cancelPaymentButton.setOnClickListener {
            viewModel.retrieveCancelBasketPayment(
                basketId
            )
        }
        binding.locationCard.setOnClickListener {
            val dialogFragment = ChoseLocationDialog.newInstance(addressesList)
            dialogFragment.show(supportFragmentManager, "YourDialogFragmentTag")
        }
        // getCart Data
        viewModel.retrieveWeeklyCart()
        getCarts()
        getSingleBasketCheckOutData()
        getAddressData()
        getCancelPaymentData()
    }

    override fun onLocationSelected(position: Int) {
        CheckOutActivity.position = position
        getAddressData()
    }

    private fun setUpRecyclerView() {
        adapter = CheckOutWeekAdapter(listOf()) { data ->

            binding.total.text = data.total_price.toString() + "JOD"
            binding.timeToArrive.text = data.start_time + "-" + data.end_time
            binding.dayToArrive.text = data.date
            binding.tax.text = data.total_tax.toString() + "JOD"
            binding.deliveryFees.text = data.delivery_fees.toString() + "JOD"
            binding.servicesFees.text = data.services_fees.toString() + "JOD"
            binding.cardView2.show()
            basketId = data.id.toString()
            if (data.payment_status == "Paid"){
                binding.checkOutButton.hide()
                binding.cancelPaymentButton.show()
            }else{
                binding.checkOutButton.show()
                binding.cancelPaymentButton.hide()
            }

        }
        val lm = LinearLayoutManager(this)
        binding.weeksOrdersRv.adapter = adapter
        binding.weeksOrdersRv.layoutManager = lm
    }

    private fun getCarts() {
        viewModel.getWeeklyCart().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter.list = result.data.data
                        adapter.setData(result.data.data)
                        adapter.notifyDataSetChanged()
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        this,
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }

    private fun getSingleBasketCheckOutData() {
        viewModel.getCheckOutSingleBasket().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {

                        HelperUtils.showMessage(this, result.data.message)
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        this,
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }
    private fun getCancelPaymentData() {
        viewModel.getCancelBasketPayment().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(this, result.data.message)
                        GlobalScope.launch {
                            delay(1000L)

                            val intent = Intent(this@CheckOutActivity , CartActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        HelperUtils.showMessage(this, result.data.message)
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        this,
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }

    private fun getAddressData() {
        viewModel.getCustomerAddresses().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {

                        addressesList = result.data.data
                        if (addressesList.isNotEmpty() && position < addressesList.size) {
                            customerAddressId = addressesList[position].id.toString()
                            binding.addressTv.text =
                                addressesList[position].area + " - " + addressesList[position].address
                        } else {
                            HelperUtils.showMessage(this, "Error")
                        }
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        this,
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.retrieveCustomerAddresses()
        getAddressData()
    }
}