package com.blueray.kees.ui.activities

import android.annotation.SuppressLint
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
import com.blueray.kees.model.WeeklyBasketData
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.fragments.ChoseLocationDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckOutActivity : BaseActivity(), OnLocationSelectedListener {
    companion object {
        var position = 0
         lateinit var basketsIds: MutableList<Int>

    }

    private lateinit var binding: ActivityCheckOutFragmentBinding
    private val viewModel: AppViewModel by viewModels()
    private lateinit var adapter: CheckOutWeekAdapter
    var basketId = ""

    private var weeksList: List<WeeklyBasketData> = listOf()
    var customerAddressId = ""
    var total = 0.0
    var subTotal = 0.0
    var deliveryFees = 0.0
    var servicesFees = 0.0
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
            val intent = Intent(this, FinishPaymentActivity::class.java)
            intent.putExtra("DeliveryFees", deliveryFees)
            intent.putExtra("ServicesFees", servicesFees)
            intent.putExtra("Total", total)
            intent.putExtra("SubTotal", subTotal)
            intent.putExtra("addressId", customerAddressId)
            Log.d("SUBSUB", subTotal.toString())
            startActivity(intent)
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
        getAddressData()
        getCancelPaymentData()
    }

    override fun onLocationSelected(position: Int) {
        CheckOutActivity.position = position
        getAddressData()
    }

    private fun setUpRecyclerView() {
        adapter = CheckOutWeekAdapter(listOf()) { data, position ->
            total += data.total_price
            deliveryFees += data.delivery_fees
            servicesFees += data.services_fees
            subTotal += data.sub_total_price
            binding.cardView2.show()

            basketsIds = mutableListOf()
            weeksList[position].selected = !weeksList[position].selected
            basketsIds.clear()
            weeksList.forEach { week ->
                // Check if the week is selected
                if (week.selected && data.payment_status != "Paid") {
                    // If selected, add its ID to the list of selected weekly basket IDs
                    basketsIds.add(week.id)

                }
            }

            // Notify the adapter of the data change
            adapter.notifyDataSetChanged()

            // Log the selected weekly basket IDs
            Log.d("WeEEEEWS", basketsIds.toString())
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
                        weeksList = result.data.data
                        adapter.list = weeksList
                        adapter.setData(weeksList)
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



    private fun getCancelPaymentData() {
        viewModel.getCancelBasketPayment().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        HelperUtils.showMessage(this, result.data.message)
                        GlobalScope.launch {
                            delay(1000L)

                            val intent = Intent(this@CheckOutActivity, CartActivity::class.java)
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

    @SuppressLint("SetTextI18n")
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