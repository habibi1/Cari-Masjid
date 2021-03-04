package com.project.masjid.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.project.masjid.ui.search_mosque.SearchMosqueAdapter

class ChooseLocationActivity : AppCompatActivity() {

    private lateinit var activityChooseLocationBinding : ActivityChooseLocationBinding

    companion object {
        private val TAG = ChooseLocationActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityChooseLocationBinding = ActivityChooseLocationBinding.inflate(layoutInflater)
        setContentView(activityChooseLocationBinding.root)

        activityChooseLocationBinding.pbDistrict.visibility = View.VISIBLE
        activityChooseLocationBinding.tilPilihKecamatan.visibility = View.GONE
        activityChooseLocationBinding.tilPilihKabupaten.visibility = View.GONE
        activityChooseLocationBinding.pbSubDistrict.visibility = View.GONE
        activityChooseLocationBinding.pbRvListMosque.visibility = View.GONE
        activityChooseLocationBinding.tvListMosque.visibility = View.GONE
        activityChooseLocationBinding.rvListMosque.visibility = View.GONE

        val db = Firebase.firestore

        var listDistrict: ArrayList<String> = ArrayList()
        var listSubDistrict: ArrayList<String> = ArrayList()
        var listMosqueFilter: ArrayList<MosqueEntity> = ArrayList()

        db.collection(getString(R.string.kabupaten_kota_))
            .get()
            .addOnSuccessListener { result ->

                Log.d("tetetes", result.documents.toString())

                listDistrict.clear()

                for (document in result) {
                    val district = document.toObject<DistrictEntity>()
                    listDistrict.add(district.districtName.toString())
                }

                activityChooseLocationBinding.pbDistrict.visibility = View.GONE
                activityChooseLocationBinding.tilPilihKecamatan.visibility = View.GONE
                activityChooseLocationBinding.tilPilihKabupaten.visibility = View.VISIBLE
                activityChooseLocationBinding.pbSubDistrict.visibility = View.GONE
                activityChooseLocationBinding.pbRvListMosque.visibility = View.GONE
                activityChooseLocationBinding.tvListMosque.visibility = View.GONE
                activityChooseLocationBinding.rvListMosque.visibility = View.GONE

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

                activityChooseLocationBinding.pbDistrict.visibility = View.GONE
                activityChooseLocationBinding.tilPilihKecamatan.visibility = View.GONE
                activityChooseLocationBinding.tilPilihKabupaten.visibility = View.VISIBLE
                activityChooseLocationBinding.pbSubDistrict.visibility = View.VISIBLE
                activityChooseLocationBinding.pbRvListMosque.visibility = View.GONE
                activityChooseLocationBinding.tvListMosque.visibility = View.GONE
                activityChooseLocationBinding.rvListMosque.visibility = View.GONE

                db.collection(getString(R.string.kecamatan)).document(s.toString()).collection(getString(R.string.daftar))
                        .get()
                        .addOnSuccessListener { result ->

                            Log.d("tetetes", result.documents.toString())

                            listSubDistrict.clear()

                            for (document in result) {
                                val subDistrict = document.toObject<SubDistrictEntity>()
                                listSubDistrict.add(subDistrict.subDistrictName.toString())
                            }

                            activityChooseLocationBinding.pbDistrict.visibility = View.GONE
                            activityChooseLocationBinding.tilPilihKecamatan.visibility = View.VISIBLE
                            activityChooseLocationBinding.tilPilihKabupaten.visibility = View.VISIBLE
                            activityChooseLocationBinding.pbSubDistrict.visibility = View.GONE
                            activityChooseLocationBinding.pbRvListMosque.visibility = View.GONE
                            activityChooseLocationBinding.tvListMosque.visibility = View.GONE
                            activityChooseLocationBinding.rvListMosque.visibility = View.GONE

                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "Error getting documents: ", exception)
                        }

                val adapterSubDistrict = ArrayAdapter(requireContext(), R.layout.item_list, listSubDistrict)
                (activityChooseLocationBinding.actPilihKecamatan as? AutoCompleteTextView)?.setAdapter(adapterSubDistrict)
            }

        })

        activityChooseLocationBinding.actPilihKecamatan.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                activityChooseLocationBinding.pbDistrict.visibility = View.GONE
                activityChooseLocationBinding.tilPilihKecamatan.visibility = View.VISIBLE
                activityChooseLocationBinding.tilPilihKabupaten.visibility = View.VISIBLE
                activityChooseLocationBinding.pbSubDistrict.visibility = View.GONE
                activityChooseLocationBinding.pbRvListMosque.visibility = View.VISIBLE
                activityChooseLocationBinding.tvListMosque.visibility = View.GONE
                activityChooseLocationBinding.rvListMosque.visibility = View.GONE

                db.collection(getString(R.string.masjid))
                    .whereEqualTo(getString(R.string.subdistrict), s.toString())
                    .get()
                    .addOnSuccessListener { result ->

                        Log.d("tetetes", result.documents.toString())

                        listMosqueFilter.clear()

                        for (document in result) {
                            val subDistrict = document.toObject<MosqueEntity>()
                            listMosqueFilter.add(subDistrict)
                        }

                        activityChooseLocationBinding.rvListMosque.layoutManager = LinearLayoutManager(this@ChooseLocationActivity)
                        activityChooseLocationBinding.rvListMosque.adapter = SearchMosqueAdapter(this@ChooseLocationActivity, listMosqueFilter)

                        activityChooseLocationBinding.pbDistrict.visibility = View.GONE
                        activityChooseLocationBinding.tilPilihKecamatan.visibility = View.VISIBLE
                        activityChooseLocationBinding.tilPilihKabupaten.visibility = View.VISIBLE
                        activityChooseLocationBinding.pbSubDistrict.visibility = View.GONE
                        activityChooseLocationBinding.pbRvListMosque.visibility = View.GONE
                        activityChooseLocationBinding.tvListMosque.visibility = View.VISIBLE
                        activityChooseLocationBinding.rvListMosque.visibility = View.VISIBLE
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
            }

        })
    }

    private fun requireContext(): Context {
        return applicationContext
    }
}