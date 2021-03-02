package com.project.masjid.ui.admin.pin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.project.masjid.R
import com.project.masjid.database.MosqueEntity
import com.project.masjid.databinding.ActivityFormAddMosqueBinding

class FormAddMosqueActivity : AppCompatActivity(), View.OnClickListener {

    private var storagePermissionGranted = false
    private var imageUri: Uri? = null
    private lateinit var mosque: MosqueEntity

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

        activityFormAddMosqueBinding.tilNameMosque.isHelperTextEnabled = false
        activityFormAddMosqueBinding.tilDescription.isHelperTextEnabled = false

        mosque = intent.getParcelableExtra<MosqueEntity>(EXTRA_MOSQUE) as MosqueEntity

        activityFormAddMosqueBinding.tfSubDistrict.setText(mosque.subDistrict)
        activityFormAddMosqueBinding.tfDistrict.setText(mosque.district)
        activityFormAddMosqueBinding.tfProvince.setText(mosque.province)
        activityFormAddMosqueBinding.tfCountry.setText(mosque.country)
        activityFormAddMosqueBinding.tfPostalCode.setText(mosque.postalCode)

        activityFormAddMosqueBinding.imgMosque.setOnClickListener(this)
        activityFormAddMosqueBinding.btnSubmit.setOnClickListener(this)
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
                if (checkField()){
                    hidden()
                    uploadData()
                }
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

    private fun checkField(): Boolean{
        if (activityFormAddMosqueBinding.tfNameMosque.text?.isEmpty() == true){
            activityFormAddMosqueBinding.tilNameMosque.isHelperTextEnabled = true
            activityFormAddMosqueBinding.tilNameMosque.helperText = getString(R.string.harus_diisi)
            return false
        } else {
            activityFormAddMosqueBinding.tilNameMosque.isHelperTextEnabled = false
        }

        if (activityFormAddMosqueBinding.tfDescription.text?.isEmpty() == true){
            activityFormAddMosqueBinding.tilDescription.isHelperTextEnabled = true
            activityFormAddMosqueBinding.tilDescription.helperText = getString(R.string.harus_diisi)
            return false
        } else {
            activityFormAddMosqueBinding.tilDescription.isHelperTextEnabled = false
        }

        if (activityFormAddMosqueBinding.tfFasilitas.text?.isEmpty() == true){
            activityFormAddMosqueBinding.tilFasilitas.isHelperTextEnabled = true
            activityFormAddMosqueBinding.tilFasilitas.helperText = getString(R.string.harus_diisi)
            return false
        } else {
            activityFormAddMosqueBinding.tilFasilitas.isHelperTextEnabled = false
        }

        if (activityFormAddMosqueBinding.tfKegiatan.text?.isEmpty() == true){
            activityFormAddMosqueBinding.tilKegiatan.isHelperTextEnabled = true
            activityFormAddMosqueBinding.tilKegiatan.helperText = getString(R.string.harus_diisi)
            return false
        } else {
            activityFormAddMosqueBinding.tilKegiatan.isHelperTextEnabled = false
        }

        if (activityFormAddMosqueBinding.tfInfoKotakAmal.text?.isEmpty() == true){
            activityFormAddMosqueBinding.tilInfoKotakAmal.isHelperTextEnabled = true
            activityFormAddMosqueBinding.tilInfoKotakAmal.helperText = getString(R.string.harus_diisi)
            return false
        } else {
            activityFormAddMosqueBinding.tilInfoKotakAmal.isHelperTextEnabled = false
        }

        if (activityFormAddMosqueBinding.tfSejarah.text?.isEmpty() == true){
            activityFormAddMosqueBinding.tilSejarah.isHelperTextEnabled = true
            activityFormAddMosqueBinding.tilSejarah.helperText = getString(R.string.harus_diisi)
            return false
        } else {
            activityFormAddMosqueBinding.tilSejarah.isHelperTextEnabled = false
        }

        if (imageUri == null){
            activityFormAddMosqueBinding.tvEmpty.visibility = View.VISIBLE
            return false
        } else {
            activityFormAddMosqueBinding.tvEmpty.visibility = View.INVISIBLE
        }

        return true
    }

    private fun hidden(){
        activityFormAddMosqueBinding.pbLoading.visibility = View.VISIBLE
        activityFormAddMosqueBinding.btnSubmit.visibility = View.INVISIBLE
        activityFormAddMosqueBinding.tvEmpty.isClickable = false
        activityFormAddMosqueBinding.tfNameMosque.isEnabled = false
        activityFormAddMosqueBinding.tfDescription.isEnabled = false
    }

    private fun show(){
        activityFormAddMosqueBinding.pbLoading.visibility = View.GONE
        activityFormAddMosqueBinding.btnSubmit.visibility = View.VISIBLE
        activityFormAddMosqueBinding.tvEmpty.isClickable = true
        activityFormAddMosqueBinding.tfNameMosque.isEnabled = true
        activityFormAddMosqueBinding.tfDescription.isEnabled = true
    }

    private fun uploadData(){

        val storage = Firebase.storage

        // [START upload_create_reference]
        // Create a storage reference from our app
        val storageRef = storage.reference

        var file = imageUri
        val ref = storageRef.child("images/${file?.lastPathSegment}")
        var uploadTask = ref.putFile(file!!)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                mosque.downloadImage = downloadUri.toString()
                mosque.name = activityFormAddMosqueBinding.tfNameMosque.text.toString()
                mosque.description = activityFormAddMosqueBinding.tfDescription.text.toString()
                mosque.fasilitas = activityFormAddMosqueBinding.tfFasilitas.text.toString()
                mosque.kegiatan = activityFormAddMosqueBinding.tfKegiatan.text.toString()
                mosque.infoKotakAmal = activityFormAddMosqueBinding.tfInfoKotakAmal.text.toString()
                mosque.sejarah = activityFormAddMosqueBinding.tfSejarah.text.toString()

                val db = Firebase.firestore

                db.collection(getString(R.string.masjid))
                    .document(mosque.province.toString())
                    .collection(getString(R.string.kabupaten_kota_))
                    .document(mosque.district.toString())
                    .collection(getString(R.string.kecamatan))
                    .document(getString(R.string.daftar_masjid))
                    .collection(mosque.subDistrict.toString())
                    .add(mosque)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, getString(R.string.berhasil_ditambahkan), Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                    }
            } else {
                show()
                // Handle failures
                // ...
            }
        }
    }
}