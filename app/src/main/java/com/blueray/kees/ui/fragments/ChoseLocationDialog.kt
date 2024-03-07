package com.blueray.kees.ui.fragments

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.ChooseLocationAdapter
import com.blueray.kees.api.OnLocationSelectedListener
import com.blueray.kees.databinding.FragmentChoseLocationDialogBinding
import com.blueray.kees.model.CustomerGetAddressesData
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.CheckOutActivity
import java.io.Serializable


class ChoseLocationDialog : DialogFragment()   {
    private lateinit var binding: FragmentChoseLocationDialogBinding
    private val viewModel:AppViewModel by viewModels()
    private lateinit var adapter: ChooseLocationAdapter
    private var listener: OnLocationSelectedListener? = null
    companion object {
        const val ARG_LIST = "argList"
        var pos = 0
        // Function to create a new instance of the dialog fragment with a list argument
        fun newInstance(yourList: List<CustomerGetAddressesData>): ChoseLocationDialog {
            val fragment = ChoseLocationDialog()
            val args = Bundle()
            args.putSerializable(ARG_LIST, yourList as Serializable)
            fragment.arguments = args
            return fragment
        }
    }
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
        // Inflate the layout for this fragment
        binding = FragmentChoseLocationDialogBinding.inflate(layoutInflater)
        initAdapter()
        adapter.onItemClick = {
            pos = it
            CheckOutActivity.position = pos
            dialog?.dismiss()
        }
        return binding.root
    }
    private fun getListFromArguments(): List<CustomerGetAddressesData>? {
        return arguments?.getSerializable(ARG_LIST) as? List<CustomerGetAddressesData>
    }
    private fun initAdapter(){
        adapter = ChooseLocationAdapter(listOf() ,{})
        adapter.list = getListFromArguments() ?: listOf()
        binding.chooseLocationRv.adapter = adapter
        binding.chooseLocationRv.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLocationSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnLocationSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onLocationSelected(pos)
        viewModel.retrieveCustomerAddresses()
    }
}
