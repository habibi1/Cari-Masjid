package com.project.masjid.ui.admin.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.project.masjid.R
import com.project.masjid.database.MosqueEntity
import com.project.masjid.databinding.ActivityEditDataMosqueBinding
import com.project.masjid.ui.search_mosque.DetailSearchMosqueActivity

class EditDataMosqueActivity : AppCompatActivity() {

    private lateinit var mosque: MosqueEntity
    private lateinit var activityEditDataMosqueBinding: ActivityEditDataMosqueBinding

    companion object {
        const val EXTRA_MOSQUE = "extra_mosque"
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

        activityEditDataMosqueBinding.btnEditMapsMosque.setOnClickListener {
            val moveWithObjectIntent = Intent(this, EditMapsActivity::class.java)
            moveWithObjectIntent.putExtra(EditMapsActivity.EXTRA_MOSQUE, mosque)
            startActivity(moveWithObjectIntent)
        }
    }
}