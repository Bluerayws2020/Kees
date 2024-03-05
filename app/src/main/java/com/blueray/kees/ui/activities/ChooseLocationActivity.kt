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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.blueray.kees.R
import com.blueray.kees.databinding.ActivityChooseLocationBinding
import com.blueray.kees.helpers.HelperUtils.AREA
import com.blueray.kees.helpers.HelperUtils.CITY
import com.blueray.kees.helpers.HelperUtils.IMAGE
import com.blueray.kees.helpers.HelperUtils.LAT
import com.blueray.kees.helpers.HelperUtils.LOCATION_IN_STRING
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.blueray.kees.helpers.ViewUtils.isInputEmpty
import java.io.File

class ChooseLocationActivity : BaseActivity() {

    private lateinit var binding : ActivityChooseLocationBinding
    private val REQUEST_CODE = 100
    private val IMAGE_REQUEST_CODE = 101
    private var  imageData : String? =null
    private var  imageFile : File? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includedTap.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
        binding.mapIcon.setOnClickListener {
            startActivity(Intent(this,MapsActivity::class.java))
        }
        binding.ownerNameET.setOnClickListener {
            startActivity(Intent(this,MapsActivity::class.java))
        }
        
        binding.includedTap.title.text= "Create Account"
        binding.previosBtn.setOnClickListener {
            onBackPressed()
        }

        binding.uploadBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2){
                if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_MEDIA_IMAGES ) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    Log.e("ayham", "permission denied")
                    requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),REQUEST_CODE)
                } else {
                    image()
                }
            }else{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    Log.e("ayham", "permission denied")
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_CODE)
                } else {
                    image()
                }}
        }

        binding.continueBtn.setOnClickListener {
            validateInput()
        }

    }

    private fun validateInput() {
        if (LAT.isEmpty()){
            showMessage(this,getString(R.string.chooseLocation))
            return
        }
        if(binding.areaEt.isInputEmpty()){
            showMessage(this,getString(R.string.enter_area))
            return
        }
        CITY = "5"
        AREA = binding.areaEt.text.toString()
        IMAGE = imageFile
        startActivity(Intent(this,UserDataRegistrationActivity::class.java))

    }

    private fun image() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
            .setMessage("It looks like you have turned off permissions"
                    + "required for this feature. It can be enable under App settings!!!")

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
        when(requestCode){
            IMAGE_REQUEST_CODE->{
                if(data != null){
                    val uri=data.data
                    imageData=getFilePathFromUri(uri)
                    imageFile= File(imageData)
                    binding.isPhotoChoosed.text = "Image Picked!"
                }else{
                    showMessage(this,"لم يتم اختيار اي صورة")
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

    override fun onResume() {
        super.onResume()
        binding.ownerNameET.text = LOCATION_IN_STRING
    }
}