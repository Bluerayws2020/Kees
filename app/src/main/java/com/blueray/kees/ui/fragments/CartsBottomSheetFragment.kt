package com.blueray.kees.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentCartsBottomSheetBinding
import com.blueray.kees.ui.activities.CartActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CartsBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentCartsBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartsBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.weeklyCartBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), CartActivity::class.java))
        }
    }
}