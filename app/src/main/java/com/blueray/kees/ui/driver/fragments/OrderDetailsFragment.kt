package com.blueray.kees.ui.driver.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentOrderDetails2Binding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.model.DriverWeeklyBasketProduct
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.DriverHomeActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OrderDetailsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentOrderDetails2Binding
    private val viewModel: AppViewModel by viewModels()
    private var basket_id: String? = null
    private lateinit var mapView: MapView
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    companion object {
        var productsList: List<DriverWeeklyBasketProduct>? = null
        var note: String? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetails2Binding.inflate(layoutInflater)
        prepareAppBar(getString(R.string.orderdetails))
        val orderId = arguments?.getString("orderId")
        val flag = arguments?.getString("fromWaiting")
        viewModel.retrieveDriverOrderDetails(orderId.toString())
        if (flag == "1") {
            binding.completeOrderButton.hide()
        }
        getData()
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        binding.mapView.setOnClickListener {
//            openGoogleMaps()
        }

        getFinishedData()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.viewProductsButton.setOnClickListener {
//            if (productsList != null) {
//                val bundle = bundleOf("productsList" to productsList)
//                findNavController().navigate(R.id.orderProductsFragment , bundle)
//            } else {
//                // Handle the case where productsList is null
//                Log.d("LOOSPP?" , ":")
//            }
//        }
        binding.completeOrderButton.setOnClickListener {
//            Toast.makeText(requireContext(), basket_id.toString(), Toast.LENGTH_LONG).show()
            viewModel.retrieveFinishOrder(basket_id.toString())

        }

    }

    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            (activity as DriverHomeActivity).onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
    }

    private fun getData() {
        viewModel.getDriverOrderDetails().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        binding.numberOfProductsTv.text =
                            result.data.data.weekly_basket_products.count().toString()
                        binding.ClintNameTv.text = result.data.data.user_name
                        binding.ClintNumberTv.text = result.data.data.user_phone
                        binding.notesTv.text = result.data.data.note ?: ""
                        binding.dayTv.text = result.data.data.day
                        binding.dateTv.text = result.data.data.date
                        binding.deliveryTimeTv.text =
                            result.data.data.start_time + " - " + result.data.data.end_time
                        // variables to pass to the product list page
                        productsList = result.data.data.weekly_basket_products
                        basket_id = result.data.data.id.toString()
                        note = result.data.data.note

                        //variables for map
                        lat = result.data.data.latitude.toDouble()
                        lng = result.data.data.longitude.toDouble()
                        Log.d("LLOOOCCc" , lat.toString() + "  "+ lng.toString())
                        binding.viewProductsButton.setOnClickListener {

                            findNavController().navigate(R.id.orderProductsFragment)
                        }
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

    private fun getFinishedData() {
        viewModel.getFinishOrder().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        val dialog = DoneDialogFragment()
                        dialog.show(childFragmentManager, "showDialog")
                        GlobalScope.launch {
                            delay(1000L)
                            val intent = Intent(requireContext(), DriverHomeActivity::class.java)
                            startActivity(intent)
                            activity?.finish()


                        }
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






    override fun onMapReady(googleMap: GoogleMap) {
        val location = LatLng(31.963158, 35.930359)
        googleMap.addMarker(MarkerOptions().position(location).title("San Siro"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.setOnMapClickListener { latLng ->
            val latitude = latLng.latitude
            val longitude = latLng.longitude

            openGoogleMaps(latitude, longitude)

        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun openGoogleMaps(latitude: Double, longitude: Double) {
        // Create a Uri with the desired location
        val gmmIntentUri = Uri.parse("google.navigation:q=31.963158,35.930359")

        // Create an Intent with the action view and the Uri
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        // Set the package to the Google Maps app
        mapIntent.setPackage("com.google.android.apps.maps")

        // Verify that the Google Maps app is available to handle the intent
        if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
            // Start the intent to open Google Maps
            startActivity(mapIntent)
        } else {
            // Handle case where Google Maps is not available
            Toast.makeText(requireContext(), "Google Maps is not installed", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
