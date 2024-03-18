package com.blueray.kees.ui.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentChangeLanguageBinding
import com.blueray.kees.helpers.ContextWrapper
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.blueray.kees.ui.activities.HomeActivity
import com.blueray.kees.ui.activities.UserTypeLoginActivity
import java.util.Locale


class ChangeLanguageFragment : DialogFragment() , View.OnClickListener{
private lateinit var binding:FragmentChangeLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onStart() {
        super.onStart()

        // Set the width and height of the dialog window
        dialog?.window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.location_dialog_width),
            resources.getDimensionPixelSize(R.dimen.location_dialog_height)
        )
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeLanguageBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        binding.arabicCard.setOnClickListener(this)
        binding.englishCard.setOnClickListener(this)
        return binding.root
    }


    override fun onClick(v: View?) {
        var language = ""
        when (v?.id) {
            R.id.englishCard -> language = "en"

            R.id.arabicCard -> language = "ar"
        }

        HelperUtils.setLang(requireContext(), language)
        if (HelperUtils.getToken(requireContext()) != "") {
            if (HelperUtils.getDriverStatus(requireContext()) != "") {
                val intent = Intent(requireContext(), DriverHomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

        }

    }



}