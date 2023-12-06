package com.blueray.kees.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.viewModels
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityUserDataRegistrationBinding
import com.blueray.kees.helpers.HelperUtils.AREA
import com.blueray.kees.helpers.HelperUtils.CONFIRM_PASSWORD
import com.blueray.kees.helpers.HelperUtils.DATE_OF_BIRTH
import com.blueray.kees.helpers.HelperUtils.EMAIL
import com.blueray.kees.helpers.HelperUtils.FULL_NAME
import com.blueray.kees.helpers.HelperUtils.GENDER
import com.blueray.kees.helpers.HelperUtils.IMAGE
import com.blueray.kees.helpers.HelperUtils.LAT
import com.blueray.kees.helpers.HelperUtils.LONG
import com.blueray.kees.helpers.HelperUtils.OTP
import com.blueray.kees.helpers.HelperUtils.PASSWORD
import com.blueray.kees.helpers.HelperUtils.saveUserData
import com.blueray.kees.helpers.HelperUtils.saveUserToken
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.blueray.kees.helpers.ViewUtils.isInputEmpty
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import java.util.*

class UserDataRegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityUserDataRegistrationBinding
    private var dateOfBirth: String = ""
    private var gender: String = "1"

    private val viewModel: AppViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDataRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // call observers
        getData()

        binding.includedTap.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.title.text = getString(R.string.create_accounttt)


        binding.continueBtn.setOnClickListener {
            verifyUserInput()
        }

        binding.genderSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // gender 1 for male and 2 for female
                gender = "${position + 1}"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // nothing
            }
        }

        binding.birthDate.setOnClickListener {
            prepareDatePickerDialog()
        }

    }

    private fun prepareDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->

            dateOfBirth = "$year-${String.format(Locale.ENGLISH, "%02d", monthOfYear + 1)}-${
                String.format(
                    Locale.ENGLISH, "%02d", dayOfMonth
                )
            }"
            binding.birthDate.text = dateOfBirth
        }, year, month, day)


        dialog.show()
    }

    private fun verifyUserInput() {
        if (binding.clientName.isInputEmpty()) {
            binding.clientName.requestFocus()
            showMessage(this, getString(R.string.enter_name))
            return
        }
        if (dateOfBirth.isEmpty()) {
            showMessage(this, getString(R.string.choose_dob))
            return
        }
        if (binding.emailEt.isInputEmpty()) {
            binding.emailEt.requestFocus()
            showMessage(this, getString(R.string.email_please))
            return
        }
        if (binding.passwordEt.isInputEmpty()) {
            binding.passwordEt.requestFocus()
            showMessage(this, getString(R.string.enter_pass))
            return
        }
        if (binding.phoneEt.isInputEmpty()) {
            binding.phoneEt.requestFocus()
            showMessage(this, getString(R.string.enter_phone))
            return
        }
        if (binding.confirmPasswordEt.isInputEmpty()) {
            binding.passwordEt.requestFocus()
            showMessage(this, getString(R.string.enter_confirm))
            return
        }
        if (binding.confirmPasswordEt.text.toString() != binding.passwordEt.text.toString()) {
            binding.passwordEt.requestFocus()
            showMessage(this, getString(R.string.passwordMatch))
            return
        }

        FULL_NAME = binding.clientName.text.toString()
        GENDER = gender
        DATE_OF_BIRTH = dateOfBirth
        EMAIL = binding.emailEt.text.toString()
        val phone = binding.phoneEt.text.toString()
        PASSWORD = binding.passwordEt.text.toString()
        CONFIRM_PASSWORD = binding.confirmPasswordEt.text.toString()

        viewModel.retrieveRegistration(
            FULL_NAME,
            GENDER,
            DATE_OF_BIRTH,
            LAT,
            LONG,
            "5",
            "Amman",// Todo change this once the api is ok
            AREA,
            PASSWORD,
            CONFIRM_PASSWORD,
            null,
            IMAGE,
            EMAIL,
            phone
        )



    }

    private fun getData() {
        viewModel.getRegistration().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        saveUserData(this,result.data.customer_data)
                        saveUserToken(this,result.data.token)
                        OTP = result.data.customer_data.otp_code.toString()
                        startActivity(Intent(this, OtpActivity::class.java))
                    } else {
                        showMessage(this, getString(R.string.Error))
                    }
                }
                is NetworkResults.ErrorMessage -> {
                    showMessage(this, result.data?.message ?: getString(R.string.Error))
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    showMessage(this, getString(R.string.Error))
                }
            }
        }
    }


}