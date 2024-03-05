package com.blueray.kees.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.blueray.kees.R


class NotesDialogFragment : DialogFragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_dialog, container, false)
    }
}