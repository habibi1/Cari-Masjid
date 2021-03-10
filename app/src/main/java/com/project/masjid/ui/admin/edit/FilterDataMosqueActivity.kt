package com.project.masjid.ui.admin.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.project.masjid.R
import com.project.masjid.database.DistrictEntity
import com.project.masjid.database.MosqueEntity
import com.project.masjid.database.SubDistrictEntity
import com.project.masjid.databinding.ActivityChooseLocationBinding
import com.project.masjid.databinding.ActivityFilterDataMosqueBinding
import com.project.masjid.ui.ChooseLocationActivity

class FilterDataMosqueActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var activityFilterDataMosqueBinding: ActivityFilterDataMosqueBinding
    private lateinit var kecamatanString: String
    private lateinit var listMosqueFilter: ArrayList<MosqueEntity>

    companion object {
        private val TAG = FilterDataMosqueActivity::class.java.simpleName
        private var status = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityFilterDataMosqueBinding = ActivityFilterDataMosqueBinding.inflate(layoutInflater)
        setContentView(activityFilterDataMosqueBinding.root)

    }

    override fun onResume() {
        super.onResume()

        activityFilterDataMosqueBinding.topAppBar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        activityFilterDataMosqueBinding.pbDistrict.visibility = View.VISIBLE
        activityFilterDataMosqueBinding.tilPilihKecamatan.visibility = View.GONE
        activityFilterDataMosqueBinding.tilPilihKabupaten.visibility = View.GONE
        activityFilterDataMosqueBinding.pbSubDistrict.visibility = View.GONE
        activityFilterDataMosqueBinding.pbRvListMosque.visibility = View.GONE
        activityFilterDataMosqueBinding.tvListMosque.visibility = View.GONE
        activityFilterDataMosqueBinding.rvListMosque.visibility = View.GONE

        var listDistrict: ArrayList<String> = ArrayList()
        var listSubDistrict: ArrayList<String> = ArrayList()
        listMosqueFilter = ArrayList()

        db.collection(getString(R.string.kabupaten_kota_))
                .get()
                .addOnSuccessListener { result ->

                    Log.d("tetetes", result.documents.toString())

                    listDistrict.clear()

                    for (document in result) {
                        val district = document.toObject<DistrictEntity>()
                        listDistrict.add(district.districtName.toString())
                    }

                    activityFilterDataMosqueBinding.pbDistrict.visibility = View.GONE
                    activityFilterDataMosqueBinding.tilPilihKecamatan.visibility = View.GONE
                    activityFilterDataMosqueBinding.tilPilihKabupaten.visibility = View.VISIBLE
                    activityFilterDataMosqueBinding.pbSubDistrict.visibility = View.GONE
                    activityFilterDataMosqueBinding.pbRvListMosque.visibility = View.GONE
                    activityFilterDataMosqueBinding.tvListMosque.visibility = View.GONE
                    activityFilterDataMosqueBinding.rvListMosque.visibility = View.GONE

                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }

        val adapter = ArrayAdapter(this, R.layout.item_list, listDistrict)
        (activityFilterDataMosqueBinding.actPilihKabupaten as? AutoCompleteTextView)?.setAdapter(adapter)

        activityFilterDataMosqueBinding.actPilihKabupaten.addTextChangedListener(object :
                TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                activityFilterDataMosqueBinding.pbDistrict.visibility = View.GONE
                activityFilterDataMosqueBinding.tilPilihKecamatan.visibility = View.GONE
                activityFilterDataMosqueBinding.tilPilihKabupaten.visibility = View.VISIBLE
                activityFilterDataMosqueBinding.pbSubDistrict.visibility = View.VISIBLE
                activityFilterDataMosqueBinding.pbRvListMosque.visibility = View.GONE
                activityFilterDataMosqueBinding.tvListMosque.visibility = View.GONE
                activityFilterDataMosqueBinding.rvListMosque.visibility = View.GONE

                db.collection(getString(R.string.kecamatan)).document(s.toString()).collection(getString(R.string.daftar))
                        .get()
                        .addOnSuccessListener { result ->

                            Log.d("tetetes", result.documents.toString())

                            listSubDistrict.clear()

                            for (document in result) {
                                val subDistrict = document.toObject<SubDistrictEntity>()
                                listSubDistrict.add(subDistrict.subDistrictName.toString())
                            }

                            activityFilterDataMosqueBinding.pbDistrict.visibility = View.GONE
                            activityFilterDataMosqueBinding.tilPilihKecamatan.visibility = View.VISIBLE
                            activityFilterDataMosqueBinding.tilPilihKabupaten.visibility = View.VISIBLE
                            activityFilterDataMosqueBinding.pbSubDistrict.visibility = View.GONE
                            activityFilterDataMosqueBinding.pbRvListMosque.visibility = View.GONE
                            activityFilterDataMosqueBinding.tvListMosque.visibility = View.GONE
                            activityFilterDataMosqueBinding.rvListMosque.visibility = View.GONE

                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "Error getting documents: ", exception)
                        }

                val adapterSubDistrict = ArrayAdapter(this@FilterDataMosqueActivity, R.layout.item_list, listSubDistrict)
                (activityFilterDataMosqueBinding.actPilihKecamatan as? AutoCompleteTextView)?.setAdapter(adapterSubDistrict)
            }

        })

        activityFilterDataMosqueBinding.actPilihKecamatan.addTextChangedListener(object :
                TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                kecamatanString = s.toString()

                activityFilterDataMosqueBinding.pbDistrict.visibility = View.GONE
                activityFilterDataMosqueBinding.tilPilihKecamatan.visibility = View.VISIBLE
                activityFilterDataMosqueBinding.tilPilihKabupaten.visibility = View.VISIBLE
                activityFilterDataMosqueBinding.pbSubDistrict.visibility = View.GONE
                activityFilterDataMosqueBinding.pbRvListMosque.visibility = View.VISIBLE
                activityFilterDataMosqueBinding.tvListMosque.visibility = View.GONE
                activityFilterDataMosqueBinding.rvListMosque.visibility = View.GONE

                status = true

                db.collection(getString(R.string.masjid))
                        .whereEqualTo(getString(R.string.subdistrict), kecamatanString)
                        .get()
                        .addOnSuccessListener { result ->

                            Log.d("tetetes", result.documents.toString())

                            listMosqueFilter.clear()

                            for (document in result) {
                                val subDistrict = document.toObject<MosqueEntity>()
                                listMosqueFilter.add(subDistrict)
                            }

                            activityFilterDataMosqueBinding.rvListMosque.layoutManager = LinearLayoutManager(this@FilterDataMosqueActivity)
                            activityFilterDataMosqueBinding.rvListMosque.adapter = FilterDataMosqueAdapter(this@FilterDataMosqueActivity, listMosqueFilter)

                            activityFilterDataMosqueBinding.pbDistrict.visibility = View.GONE
                            activityFilterDataMosqueBinding.tilPilihKecamatan.visibility = View.VISIBLE
                            activityFilterDataMosqueBinding.tilPilihKabupaten.visibility = View.VISIBLE
                            activityFilterDataMosqueBinding.pbSubDistrict.visibility = View.GONE
                            activityFilterDataMosqueBinding.pbRvListMosque.visibility = View.GONE
                            activityFilterDataMosqueBinding.tvListMosque.visibility = View.VISIBLE
                            activityFilterDataMosqueBinding.rvListMosque.visibility = View.VISIBLE
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "Error getting documents: ", exception)
                        }
            }

        })
    }

    fun replaceListMosque(){
        activityFilterDataMosqueBinding.pbDistrict.visibility = View.GONE
        activityFilterDataMosqueBinding.tilPilihKecamatan.visibility = View.VISIBLE
        activityFilterDataMosqueBinding.tilPilihKabupaten.visibility = View.VISIBLE
        activityFilterDataMosqueBinding.pbSubDistrict.visibility = View.GONE
        activityFilterDataMosqueBinding.pbRvListMosque.visibility = View.VISIBLE
        activityFilterDataMosqueBinding.tvListMosque.visibility = View.GONE
        activityFilterDataMosqueBinding.rvListMosque.visibility = View.GONE

        db.collection(getString(R.string.masjid))
                .whereEqualTo(getString(R.string.subdistrict), kecamatanString)
                .get()
                .addOnSuccessListener { result ->

                    Log.d("tetetes", result.documents.toString())

                    listMosqueFilter.clear()

                    for (document in result) {
                        val subDistrict = document.toObject<MosqueEntity>()
                        listMosqueFilter.add(subDistrict)
                    }

                    activityFilterDataMosqueBinding.rvListMosque.layoutManager = LinearLayoutManager(this@FilterDataMosqueActivity)
                    activityFilterDataMosqueBinding.rvListMosque.adapter = FilterDataMosqueAdapter(this@FilterDataMosqueActivity, listMosqueFilter)

                    activityFilterDataMosqueBinding.pbDistrict.visibility = View.GONE
                    activityFilterDataMosqueBinding.tilPilihKecamatan.visibility = View.VISIBLE
                    activityFilterDataMosqueBinding.tilPilihKabupaten.visibility = View.VISIBLE
                    activityFilterDataMosqueBinding.pbSubDistrict.visibility = View.GONE
                    activityFilterDataMosqueBinding.pbRvListMosque.visibility = View.GONE
                    activityFilterDataMosqueBinding.tvListMosque.visibility = View.VISIBLE
                    activityFilterDataMosqueBinding.rvListMosque.visibility = View.VISIBLE
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
    }
}