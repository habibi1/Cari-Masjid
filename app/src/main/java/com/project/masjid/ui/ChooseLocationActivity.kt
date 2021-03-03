package com.project.masjid.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.project.masjid.R
import com.project.masjid.database.DistrictEntity
import com.project.masjid.database.MosqueEntity
import com.project.masjid.database.SubDistrictEntity
import com.project.masjid.databinding.ActivityChooseLocationBinding
import com.project.masjid.ui.near_mosque.NearMosqueMapsActivity

class ChooseLocationActivity : AppCompatActivity() {

    private lateinit var activityChooseLocationBinding : ActivityChooseLocationBinding

    companion object {
        private val TAG = ChooseLocationActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityChooseLocationBinding = ActivityChooseLocationBinding.inflate(layoutInflater)
        setContentView(activityChooseLocationBinding.root)

        val db = Firebase.firestore

        var listDistrict: ArrayList<String> = ArrayList()
        var listSubDistrict: ArrayList<String> = ArrayList()

        db.collection(getString(R.string.kabupaten_kota_))
            .get()
            .addOnSuccessListener { result ->

                Log.d("tetetes", result.documents.toString())

                for (document in result) {
                    val district = document.toObject<DistrictEntity>()
                    listDistrict.add(district.districtName.toString())
                }

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        val adapter = ArrayAdapter(requireContext(), R.layout.item_list, listDistrict)
        (activityChooseLocationBinding.actPilihKabupaten as? AutoCompleteTextView)?.setAdapter(adapter)

        activityChooseLocationBinding.actPilihKabupaten.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                db.collection(getString(R.string.kecamatan)).document(s.toString()).collection(getString(R.string.daftar))
                        .get()
                        .addOnSuccessListener { result ->

                            Log.d("tetetes", result.documents.toString())

                            for (document in result) {
                                val subDistrict = document.toObject<SubDistrictEntity>()
                                listSubDistrict.add(subDistrict.subDistrictName.toString())
                            }

                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "Error getting documents: ", exception)
                        }

                val adapterSubDistrict = ArrayAdapter(requireContext(), R.layout.item_list, listSubDistrict)
                (activityChooseLocationBinding.actPilihKecamatan as? AutoCompleteTextView)?.setAdapter(adapterSubDistrict)
            }

        })
    }

    private fun requireContext(): Context {
        return applicationContext
    }
}