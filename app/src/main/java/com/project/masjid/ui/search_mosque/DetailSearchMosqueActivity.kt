package com.project.masjid.ui.search_mosque

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.project.masjid.R
import com.project.masjid.database.MosqueEntity
import com.project.masjid.databinding.ActivityDetailSearchMosqueBinding
import com.project.masjid.databinding.ActivityFormAddMosqueBinding
import com.project.masjid.ui.admin.pin.FormAddMosqueActivity

class DetailSearchMosqueActivity : AppCompatActivity() {

    private lateinit var mosque: MosqueEntity
    private lateinit var activityDetailSearchMosqueBinding: ActivityDetailSearchMosqueBinding

    companion object {
        const val EXTRA_MOSQUE = "extra_mosque"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailSearchMosqueBinding = ActivityDetailSearchMosqueBinding.inflate(layoutInflater)
        setContentView(activityDetailSearchMosqueBinding.root)

        activityDetailSearchMosqueBinding.topAppBar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        mosque = intent.getParcelableExtra<MosqueEntity>(EXTRA_MOSQUE) as MosqueEntity

        Glide.with(this)
            .load(mosque.downloadImage)
            .into(activityDetailSearchMosqueBinding.imgMosque)

        activityDetailSearchMosqueBinding.tfNameMosque.setText(mosque.name)
        activityDetailSearchMosqueBinding.tfDescription.setText(mosque.description)
        activityDetailSearchMosqueBinding.tfFasilitas.setText(mosque.fasilitas)
        activityDetailSearchMosqueBinding.tfKegiatan.setText(mosque.kegiatan)
        activityDetailSearchMosqueBinding.tfInfoKotakAmal.setText(mosque.infoKotakAmal)
        activityDetailSearchMosqueBinding.tfSejarah.setText(mosque.sejarah)
        activityDetailSearchMosqueBinding.tfSubDistrict.setText(mosque.subDistrict)
        activityDetailSearchMosqueBinding.tfDistrict.setText(mosque.district)
        activityDetailSearchMosqueBinding.tfProvince.setText(mosque.province)
        activityDetailSearchMosqueBinding.tfCountry.setText(mosque.country)
        activityDetailSearchMosqueBinding.tfPostalCode.setText(mosque.postalCode)
    }
}