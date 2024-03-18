package com.blueray.kees.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityCreateAccountBinding
import com.blueray.kees.databinding.SpinnerItemBinding

class CreateAccountActivity : BaseActivity() {

    companion object {
        fun getAccountTypesList(context: Context): List<String> {
            return listOf(
                context.getString(R.string.customer),
                context.getString(R.string.seller),

            )
        }
    }

    var selectedAccountType = ""
    var ACCOUNTS_TYPES_LIST :List<String> = listOf()
    private lateinit var binding: ActivityCreateAccountBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ACCOUNTS_TYPES_LIST  = getAccountTypesList(this)
        binding.includedTap.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.title.text= getString(R.string.create_accounttt)

        setupSpinner()

        binding.continueBtn.setOnClickListener {
            if(selectedAccountType == ACCOUNTS_TYPES_LIST[0] ){
                startActivity(Intent(this, ChooseLocationActivity::class.java))
            }else{
                startActivity(Intent(this, ChooseLocationActivity::class.java))
            }

        }
        binding.contactSupport.setOnClickListener{
            startActivity(Intent(this,ContactUsActivity::class.java))
        }
    }

    private fun setupSpinner() {
        binding.accountTypeSpinner.adapter = object : ArrayAdapter<String>(
            this, R.layout.spinner_item,
            ACCOUNTS_TYPES_LIST
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {

                val view =SpinnerItemBinding.inflate(layoutInflater)
                val item = getItem(position)

//                view.icon.hide()
                view.title.text = item

                return view.root
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val view =SpinnerItemBinding.inflate(layoutInflater)
                val item = getItem(position)

//                view.icon.hide()
                view.title.text = item

                return view.root

            }
        }

        binding.accountTypeSpinner.onItemSelectedListener = object :OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedAccountType = ACCOUNTS_TYPES_LIST[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // nothing
            }

        }

    }

}