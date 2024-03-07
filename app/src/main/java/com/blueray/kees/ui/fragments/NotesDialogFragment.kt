package com.blueray.kees.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentNotesDialogBinding


class NotesDialogFragment : DialogFragment() {
    private lateinit var binding:FragmentNotesDialogBinding
    companion object{
        var note :String? =null
    }
    override fun onStart() {
        super.onStart()

        // Set the width and height of the dialog window
        dialog?.window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.dialog_width),
            resources.getDimensionPixelSize(R.dimen.dialog_height)
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
        binding = FragmentNotesDialogBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        binding.sendButton.setOnClickListener {
            note = binding.notesEt.text.toString()
            dialog?.dismiss()
        }
        return binding.root
    }
}