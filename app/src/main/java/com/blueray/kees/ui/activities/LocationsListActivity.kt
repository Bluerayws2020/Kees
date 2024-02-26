package com.blueray.kees.ui.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityLocationsListBinding

class LocationsListActivity : AppCompatActivity() {

private lateinit var binding: ActivityLocationsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init Adapter

    }

}