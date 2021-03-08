package com.project.masjid.ui.admin.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.project.masjid.R
import com.project.masjid.database.DistrictEntity
import com.project.masjid.database.MosqueEntity
import com.project.masjid.database.SubDistrictEntity
import com.project.masjid.databinding.ActivityEditDataMosqueBinding
import com.project.masjid.ui.admin.pin.FormAddMosqueActivity

class EditDataMosqueActivity : AppCompatActivity(), View.OnClickListener {

    private var storagePermissionGranted = false
    private var imageUri: Uri? = null
    private lateinit var mosque: MosqueEntity
    private lateinit var subDistrictEntity: SubDistrictEntity
    private lateinit var districtEntity: DistrictEntity
    private lateinit var activityEditDataMosqueBinding: ActivityEditDataMosqueBinding

    companion object {
        private const val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
        private val PICK_IMAGE = 100
        const val EXTRA_MOSQUE = "extra_mosque"
        lateinit var mosqueData: MosqueEntity
        var statusMaps = false
        var statusImage = false
        private val TAG = EditDataMosqueActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activityEditDataMosqueBinding = ActivityEditDataMosqueBinding.inflate(layoutInflater)
        setContentView(activityEditDataMosqueBinding.root)

        mosque = intent.getParcelableExtra<MosqueEntity>(EXTRA_MOSQUE) as MosqueEntity

        Glide.with(this)
            .load(mosque.downloadImage)
            .into(activityEditDataMosqueBinding.imgMosque)

        activityEditDataMosqueBinding.tfNameMosque.setText(mosque.name)
        activityEditDataMosqueBinding.tfDescription.setText(mosque.description)
        activityEditDataMosqueBinding.tfFasilitas.setText(mosque.fasilitas)
        activityEditDataMosqueBinding.tfKegiatan.setText(mosque.kegiatan)
        activityEditDataMosqueBinding.tfInfoKotakAmal.setText(mosque.infoKotakAmal)
        activityEditDataMosqueBinding.tfSejarah.setText(mosque.sejarah)
        activityEditDataMosqueBinding.tfSubDistrict.setText(mosque.subDistrict)
        activityEditDataMosqueBinding.tfDistrict.setText(mosque.district)
        activityEditDataMosqueBinding.tfProvince.setText(mosque.province)
        activityEditDataMosqueBinding.tfCountry.setText(mosque.country)
        activityEditDataMosqueBinding.tfPostalCode.setText(mosque.postalCode)

        activityEditDataMosqueBinding.btnEditMapsMosque.setOnClickListener (this)
        activityEditDataMosqueBinding.btnEditDataMasjid.setOnClickListener(this)
        activityEditDataMosqueBinding.imgMosque.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        if (statusMaps){
            updateField(mosqueData)
        }

        statusMaps = false
    }

    fun updateField(mosqueEntity: MosqueEntity){
        activityEditDataMosqueBinding.tfSubDistrict.setText(mosqueEntity.subDistrict)
        activityEditDataMosqueBinding.tfDistrict.setText(mosqueEntity.district)
        activityEditDataMosqueBinding.tfProvince.setText(mosqueEntity.province)
        activityEditDataMosqueBinding.tfCountry.setText(mosqueEntity.country)
        activityEditDataMosqueBinding.tfPostalCode.setText(mosqueEntity.postalCode)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_edit_maps_mosque -> {
                val moveWithObjectIntent = Intent(this, EditMapsActivity::class.java)
                moveWithObjectIntent.putExtra(EditMapsActivity.EXTRA_MOSQUE, mosque)
                startActivity(moveWithObjectIntent)
            }
            R.id.img_mosque -> {
                openGallery()
            }
            R.id.btn_edit_data_masjid -> {
                hidden()
                if (statusImage){
                    uploadImage()
                } else {
                    updateData()
                }
            }
        }
    }

    fun uploadImage() {

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
                updateData()
            } else {
                show()
                Toast.makeText(this, getString(R.string.gagal_menambahkan), Toast.LENGTH_SHORT)
                        .show()
                // Handle failures
                // ...
            }
        }
    }

    fun updateData() {
        val db = Firebase.firestore

        val setKecamatan = db.collection(getString(R.string.kecamatan)).document(mosque.district.toString()).collection(getString(R.string.daftar)).document(mosque.subDistrict.toString())
        val setKabupatenKota = db.collection(getString(R.string.kabupaten_kota_)).document(mosque.district.toString())
        val setMasjid = db.collection(getString(R.string.masjid)).document()

        mosque.id = setMasjid.id
        subDistrictEntity = SubDistrictEntity(mosque.subDistrict)
        districtEntity = DistrictEntity(mosque.district)

        db.runBatch { batch ->
            batch.set(setKecamatan, subDistrictEntity)
            batch.set(setKabupatenKota, districtEntity)
            batch.set(setMasjid, mosque)
        }.addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this, getString(R.string.berhasil_ditambahkan), Toast.LENGTH_SHORT)
                        .show()
                finish()
            }
        }.addOnFailureListener{exception ->
            Toast.makeText(this, getString(R.string.gagal_menambahkan), Toast.LENGTH_SHORT)
                    .show()
            Log.d(TAG, "get failed with ", exception)
            show()
        }
    }

    fun hidden(){
        activityEditDataMosqueBinding.pbLoading.visibility = View.VISIBLE
        activityEditDataMosqueBinding.btnEditDataMasjid.visibility = View.INVISIBLE
        activityEditDataMosqueBinding.tfNameMosque.isEnabled = false
        activityEditDataMosqueBinding.tfDescription.isEnabled = false
        activityEditDataMosqueBinding.tfFasilitas.isEnabled = false
        activityEditDataMosqueBinding.tfKegiatan.isEnabled = false
        activityEditDataMosqueBinding.tfInfoKotakAmal.isEnabled = false
        activityEditDataMosqueBinding.tfSejarah.isEnabled = false
    }

    fun show(){
        activityEditDataMosqueBinding.pbLoading.visibility = View.GONE
        activityEditDataMosqueBinding.btnEditDataMasjid.visibility = View.VISIBLE
        activityEditDataMosqueBinding.tfNameMosque.isEnabled = true
        activityEditDataMosqueBinding.tfDescription.isEnabled = true
        activityEditDataMosqueBinding.tfFasilitas.isEnabled = true
        activityEditDataMosqueBinding.tfKegiatan.isEnabled = true
        activityEditDataMosqueBinding.tfInfoKotakAmal.isEnabled = true
        activityEditDataMosqueBinding.tfSejarah.isEnabled = true
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
                    openGallery()
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
            activityEditDataMosqueBinding.imgMosque.setImageURI(imageUri)
            statusImage = true
        }
    }
}