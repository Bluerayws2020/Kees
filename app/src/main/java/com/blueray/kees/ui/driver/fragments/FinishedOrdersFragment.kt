package com.blueray.kees.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.FinishedOrdersAdapter
import com.blueray.kees.databinding.FragmentFinishedOrdersBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel

class FinishedOrdersFragment : Fragment() {
    private var _binding: FragmentFinishedOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FinishedOrdersAdapter
    private val viewModel: AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareAppBar("Finished Orders")
        viewModel.retrieveFinishedOrders()
        getFinishedOrdersData()
    }

    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

    fun initAdapter() {
        binding.finishedOrdersRv.adapter = adapter
        binding.finishedOrdersRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun getFinishedOrdersData() {
        viewModel.getFinishedOrders().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter = FinishedOrdersAdapter(result.data.data) { orderId ->
                            // Handle item click here
                            val bundle = bundleOf("orderId" to orderId.toString(), "fromFinished" to "1")
                            findNavController().navigate(R.id.orderDetailsFragment , bundle)
                        }
                        initAdapter()
                        Log.d("MNBVC", adapter.list.size.toString())
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                    }
                }
                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(requireContext(), result.data?.message ?: getString(R.string.Error))
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
