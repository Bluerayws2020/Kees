package com.blueray.kees.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blueray.kees.R
import com.blueray.kees.ui.activities.HomeActivity


class SearchFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        (requireContext() as HomeActivity).setSelectedId(R.id.searchFragment)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

}