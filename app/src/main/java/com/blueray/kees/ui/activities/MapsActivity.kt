package com.blueray.kees.ui.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log.e
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityMapsBinding
import com.blueray.kees.helpers.HelperUtils.LAT
import com.blueray.kees.helpers.HelperUtils.LONG
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

class MapsActivity : BaseActivity(), OnMapReadyCallback, LocationListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraIdleListener {

    private val MAP_VIEW_BUNDLE_KEY: String = "map_view_bundle"
    private var mMap: GoogleMap? = null
    lateinit var mapView: MapView
    private lateinit var binding: ActivityMapsBinding
    private val DEFAULT_ZOOM = 15F
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onMapReady(googleMap: GoogleMap) {
        mapView.onResume()
        mMap = googleMap

        getTheCurrentLocation()

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap!!.isMyLocationEnabled = true
        mMap!!.setOnCameraIdleListener(this)
        mMap!!.setOnCameraMoveListener(this)
        mMap!!.setOnCameraMoveStartedListener(this)



    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includedTap.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.title.text = getString(R.string.create_accounttt)

        mapView = binding.map

        askLocationPermission()
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        binding.confirmLocationButton.setOnClickListener {
            LAT = mMap!!.cameraPosition.target.latitude.toString()
            LONG = mMap!!.cameraPosition.target.longitude.toString()
            // go back
            onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        askLocationPermission()

        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)

    }

    private fun askLocationPermission() {
        askPermission(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,

            ) {
            getTheCurrentLocation()
        }.onDeclined { e ->
            if (e.hasDenied()) {
                e.denied.forEach {

                }
                AlertDialog.Builder(this)
                    .setMessage("Please Accept Location Permissions")
                    .setPositiveButton("yes") { _, _ ->
                        e.askAgain()
                    }.setNegativeButton("no") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getTheCurrentLocation() {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        try {
            val location = fusedLocationProviderClient!!.lastLocation

            location.addOnCompleteListener { loc ->
                if (loc.isSuccessful) {
                    val currentLocation = loc.result as Location?
                    if (currentLocation != null) {
                        moveCamera(
                            LatLng(currentLocation.latitude, currentLocation.longitude),
                        )
                    }
                } else {
                    showMessage(this, "Current Location Not Found")
                    askLocationPermission()
                }
            }
        } catch (e: Exception) {
            e("ayham", e.toString())
        }

    }

    private fun moveCamera(latLng: LatLng) {
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
    }

    override fun onLocationChanged(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        setAddress(addresses!![0])
    }

    private fun setAddress(addresses: Address) {
        if (addresses != null) {
            if (addresses.getAddressLine(0) != null) {
                binding.locationTv.text = addresses.getAddressLine(0)
            }
            if (addresses.getAddressLine(1) != null) {
                binding.locationTv.text =
                    binding.locationTv.text.toString() + addresses.getAddressLine(1) }
        }
    }

    override fun onCameraMove() {
    }

    override fun onCameraMoveStarted(p0: Int) {
    }

    override fun onCameraIdle() {
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(
                mMap!!.cameraPosition.target.latitude,
                mMap!!.cameraPosition.target.longitude,
                1
            )
            setAddress(addresses!![0])
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: java.lang.IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }


}