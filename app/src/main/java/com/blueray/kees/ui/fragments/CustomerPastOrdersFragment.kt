package com.blueray.kees.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.PastOrdersAdapter
import com.blueray.kees.databinding.FragmentCustomerPastOrdersBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.HomeActivity

class CustomerPastOrdersFragment : Fragment() {

    private lateinit var binding: FragmentCustomerPastOrdersBinding
    private val viewModel: AppViewModel by viewModels()
    private lateinit var adapter: PastOrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentCustomerPastOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareAppBar(getString(R.string.past_orders))

        viewModel.retrieveCustomerPastOrders()
        getPastOrdersData()

    }


    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

    private fun getPastOrdersData() {
        viewModel.getCustomerPastOrders().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        adapter = PastOrdersAdapter(result.data.data) { orderId ->
                            val bundle = Bundle().apply {
                                putString("orderId", orderId.toString())
                                putString("fromFinished", "1")
                            }
                            findNavController()
                                .navigate(R.id.pastOrderDetailsFragment, bundle)
                        }
                        binding.pastOrdersRv.adapter = adapter
                        binding.pastOrdersRv.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        requireContext(),
                        result.data?.message ?: getString(R.string.Error)
                    )
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }
}
