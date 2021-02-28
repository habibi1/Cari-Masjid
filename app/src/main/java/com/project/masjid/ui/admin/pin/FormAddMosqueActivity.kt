package com.project.masjid.ui.admin.pin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.project.masjid.R
import com.project.masjid.database.MosqueEntity
import com.project.masjid.databinding.ActivityFormAddMosqueBinding

class FormAddMosqueActivity : AppCompatActivity(), View.OnClickListener {

    private var storagePermissionGranted = false
    private var imageUri: Uri? = null

    private lateinit var activityFormAddMosqueBinding: ActivityFormAddMosqueBinding

    companion object {
        const val EXTRA_MOSQUE = "extra_mosque"
        private const val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
        private val PICK_IMAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFormAddMosqueBinding = ActivityFormAddMosqueBinding.inflate(layoutInflater)
        setContentView(activityFormAddMosqueBinding.root)

        val mosque = intent.getParcelableExtra<MosqueEntity>(EXTRA_MOSQUE) as MosqueEntity

        activityFormAddMosqueBinding.tfSubDistrict.setText(mosque.subDistrict)
        activityFormAddMosqueBinding.tfDistrict.setText(mosque.district)
        activityFormAddMosqueBinding.tfProvince.setText(mosque.province)
        activityFormAddMosqueBinding.tfCountry.setText(mosque.country)
        activityFormAddMosqueBinding.tfPostalCode.setText(mosque.postalCode)

        activityFormAddMosqueBinding.imgMosque.setOnClickListener(this)
    }

    private fun getStoragePermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            storagePermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        storagePermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storagePermissionGranted = true
                }
            }
        }

        openGallery()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.img_mosque -> {
                openGallery()
            }
            R.id.btn_submit -> {

            }
        }
    }

    private fun openGallery(){
        if (storagePermissionGranted){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        } else {
            getStoragePermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data?.data
            activityFormAddMosqueBinding.imgMosque.setImageURI(imageUri)
        }
    }
}