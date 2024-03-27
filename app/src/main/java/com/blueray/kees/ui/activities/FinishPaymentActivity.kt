package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityFinishPaymentBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FinishPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishPaymentBinding
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareAppBar("Payment Details")
        val deliveryFees = intent.getDoubleExtra("DeliveryFees", 0.0)
        val servicesFees = intent.getDoubleExtra("ServicesFees", 0.0)
        val total = intent.getDoubleExtra("Total", 0.0)
        val subTotal = intent.getDoubleExtra("SubTotal", 0.0)
        val addressId = intent.getStringExtra("addressId")
        binding.deliveryFeesTv.text = deliveryFees.toString() + "JOD"
        binding.servicesFeesTv.text = servicesFees.toString() + "JOD"
        binding.totalPrice.text = total.toString() + "JOD"
        binding.subTotalPrice.text = subTotal.toString() + "JOD"
        binding.checkOutButton.setOnClickListener {
            if (addressId != null) {
                viewModel.retrieveCheckOutMultiBaskets(
                    CheckOutActivity.basketsIds,
                    addressId
                )
            }
        }
        getMultiBasketCheckOutData()
    }

    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

    private fun getMultiBasketCheckOutData() {
        viewModel.getCheckOutMultiBaskets().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {

                        HelperUtils.showMessage(
                            this,
                            result.data.message ?: result.data.data.toString()
                        )
                        GlobalScope.launch {
                            delay(500)
                            startActivity(
                                Intent(
                                    this@FinishPaymentActivity,
                                    HomeActivity::class.java
                                )
                            )
                            finishAffinity()
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
}