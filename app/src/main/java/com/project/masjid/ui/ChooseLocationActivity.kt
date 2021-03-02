package com.project.masjid.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.project.masjid.R
import com.project.masjid.database.MosqueEntity
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

        db.collection(getString(R.string.masjid))
            .document(getString(R.string.lampung))
            .collection(getString(R.string.kabupaten_kota_))
            .get()
            .addOnSuccessListener { result ->

                var listDistrict: ArrayList<String>? = null

                if (result.isEmpty) {

                } else {
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        listDistrict?.add(document.data.toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }


        val items = listOf("Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_list, items)
        (activityChooseLocationBinding.actPilihKabupaten as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun requireContext(): Context {
        return applicationContext
    }
}