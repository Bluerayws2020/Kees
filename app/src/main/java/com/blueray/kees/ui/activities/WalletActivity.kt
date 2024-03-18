package com.blueray.kees.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.WalletAdapter
import com.blueray.kees.databinding.ActivityWalletBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class WalletActivity : BaseActivity() {
    private lateinit var binding: ActivityWalletBinding
    private val viewModel: AppViewModel by viewModels()
    private lateinit var adapter: WalletAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set Up app bar
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.retrieveWalletTransaction()
        getData()
        binding.balance.text = intent.getStringExtra("wallet")
    }

    private fun getData(){
        viewModel.getWalletTransaction().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter = WalletAdapter(result.data.data)
                        val lm = LinearLayoutManager(this)
                        binding.walletRv.adapter = adapter
                        binding.walletRv.layoutManager = lm
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