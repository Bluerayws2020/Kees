package com.blueray.kees.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentNotificationPopUpBinding


class NotificationPopUp : DialogFragment() {
    private lateinit var binding: FragmentNotificationPopUpBinding

    companion object {
        const val ARG_STRING1 = "argString1"
        const val ARG_STRING2 = "argString2"

        fun newInstance(string1: String, string2: String): NotificationPopUp {
            val fragment = NotificationPopUp()
            val args = Bundle().apply {
                putString(ARG_STRING1, string1)
                putString(ARG_STRING2, string2)
            }
            fragment.arguments = args
            return fragment
        }
    }
    override fun onStart() {
        super.onStart()

        // Set the width and height of the dialog window
        val window = dialog?.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }

    private var string1: String? = null
    private var string2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            string1 = it.getString(ARG_STRING1)
            string2 = it.getString(ARG_STRING2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationPopUpBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        binding.dateTv.text = string2
        binding.notificationTv.text = string1
        return binding.root
    }

}