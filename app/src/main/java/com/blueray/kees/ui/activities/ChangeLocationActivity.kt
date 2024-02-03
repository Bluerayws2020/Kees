package com.blueray.kees.ui.activities

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityChangeLocationBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.AREA
import com.blueray.kees.helpers.HelperUtils.CITY
import com.blueray.kees.helpers.HelperUtils.LAT
import com.blueray.kees.helpers.HelperUtils.LONG
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.blueray.kees.helpers.ViewUtils.isInputEmpty
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import java.io.File

class ChangeLocationActivity : BaseActivity() {
    val viewModel: AppViewModel by viewModels()
    private lateinit var binding: ActivityChangeLocationBinding
    private val REQUEST_CODE = 100
    private val IMAGE_REQUEST_CODE = 101
    private var imageData: String? = null
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includedTap.title.text = getString(R.string.change_location)

        binding.includedTap.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.mapIcon.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.includedTap.title.text = getString(R.string.create_accounttt)
        binding.previosBtn.setOnClickListener {
            onBackPressed()
        }

        binding.uploadBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is not granted, request it
                    Log.e("ayham", "permission denied")
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                        REQUEST_CODE
                    )
                } else {
                    image()
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is not granted, request it
                    Log.e("ayham", "permission denied")
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE
                    )
                } else {
                    image()
                }
            }
        }

        binding.continueBtn.setOnClickListener {
            validateInput()
        }

        // observe to live Data
        getData()

    }

    private fun validateInput() {
        if (HelperUtils.LAT.isEmpty()) {
            HelperUtils.showMessage(this, getString(R.string.chooseLocation))
            return
        }
        if (binding.areaEt.isInputEmpty()) {
            HelperUtils.showMessage(this, getString(R.string.enter_area))
            return
        }
        HelperUtils.CITY = "5" // todo add Cities
        HelperUtils.AREA = binding.areaEt.text.toString()
        HelperUtils.IMAGE = imageFile
        viewModel.retrieveCustomerUpdateAddress(
            intent.getStringExtra("address_id") ?: "",
            intent.getStringExtra("title") ?: "",
            LAT,
            LONG,
            CITY,
            AREA,
            binding.addressInDetailEt.text.toString()
        )
    }

    private fun image() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    image()
                } else {
                    showRotationalDialogForPermission()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage(
                "It looks like you have turned off permissions"
                        + "required for this feature. It can be enable under App settings!!!"
            )

            .setPositiveButton("Go TO SETTINGS") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }

            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST_CODE -> {
                if (data != null) {
                    val uri = data.data
                    imageData = getFilePathFromUri(uri)
                    imageFile = File(imageData)
                    binding.isPhotoChoosed.text = "Image Picked!"
                } else {
                    HelperUtils.showMessage(this, "لم يتم اختيار اي صورة")
                }
            }
        }
    }

    private fun getFilePathFromUri(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)

        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            filePath
        } else {
            uri.path ?: ""
        }
    }

    private fun getData(){
        viewModel.getCustomerUpdateAddress().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        showMessage(this,result.data.message ?:"Address Updated")
                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }

                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(this, result.data?.message ?: getString(R.string.Error))
                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }
}