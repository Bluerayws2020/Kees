package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.CheckOutWeekAdapter
import com.blueray.kees.databinding.ActivityCheckOutFragmentBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.ADDRESSES_LIST
import com.blueray.kees.helpers.HelperUtils.SELECTED_ADDRESS
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.CustomerGetAddressesData
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class CheckOutActivity : BaseActivity() {

    private var addressesList: List<CustomerGetAddressesData> = listOf()
    private lateinit var binding: ActivityCheckOutFragmentBinding
    private val viewModel: AppViewModel by viewModels()
    private lateinit var adapter: CheckOutWeekAdapter

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

        // init Adapter
        setUpRecyclerView()

        // getCart Data
        viewModel.retrieveWeeklyCart()
        getMyAddresses()
        getCarts()
        binding.chooseAddressBtn.setOnClickListener{
            startActivity(Intent(this,ChooseLocationActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        // call API When ever You come back to this Activity
        viewModel.retrieveCustomerAddresses()

    }

    private fun setUpRecyclerView() {
        adapter = CheckOutWeekAdapter(listOf()) { data ->
            binding.total.text = data.total_price.toString() + "JOD"
            binding.timeToArrive.text = data.start_time + "-" + data.end_time
            binding.dayToArrive.text = data.date
            binding.tax.text = data.total_tax.toString() + "JOD"
            binding.cardView2.show()
        }
        val lm = LinearLayoutManager(this)
        binding.weeksOrdersRv.adapter = adapter
        binding.weeksOrdersRv.layoutManager = lm
    }

    private fun getMyAddresses(){
        viewModel.getCustomerAddresses().observe(this){
            result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                       addressesList = result.data.data
                        if(SELECTED_ADDRESS == null){
                            SELECTED_ADDRESS= addressesList.first()
                        }
                        binding.addressLine.text = SELECTED_ADDRESS?.address
                        binding.city.text = SELECTED_ADDRESS?.city_name
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
    private fun getCarts() {
        viewModel.getWeeklyCart().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter.list = result.data.data
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

}