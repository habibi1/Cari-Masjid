package com.project.masjid.ui.admin.edit

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.project.masjid.R
import com.project.masjid.database.MosqueEntity
import com.project.masjid.databinding.ActivityEditMapsBinding
import com.project.masjid.ui.near_mosque.NearMosqueMapsActivity
import java.util.*

class EditMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mosqueData: MosqueEntity
    private lateinit var activityEditMapsBinding: ActivityEditMapsBinding

    companion object {
        const val EXTRA_MOSQUE = "extra_mosque"
        private const val DEFAULT_ZOOM = 15f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditMapsBinding = ActivityEditMapsBinding.inflate(layoutInflater)
        setContentView(activityEditMapsBinding.root)

        hiddenMaps()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mosqueData = intent.getParcelableExtra<MosqueEntity>(EXTRA_MOSQUE) as MosqueEntity

        activityEditMapsBinding.btnConfirm.setOnClickListener(this)

        activityEditMapsBinding.topAppBar.setNavigationOnClickListener {
            this.onBackPressed()
        }
    }

    private fun hiddenMaps(){
        activityEditMapsBinding.btnConfirm.visibility = View.INVISIBLE
        activityEditMapsBinding.map.visibility = View.INVISIBLE
        activityEditMapsBinding.pbMaps.visibility = View.VISIBLE
        activityEditMapsBinding.tvStatus.visibility = View.VISIBLE
        activityEditMapsBinding.tvStatus.text = getString(R.string.mencari_lokasi)
    }

    private fun showMaps(){
        activityEditMapsBinding.btnConfirm.visibility = View.INVISIBLE
        activityEditMapsBinding.map.visibility = View.VISIBLE
        activityEditMapsBinding.pbMaps.visibility = View.INVISIBLE
        activityEditMapsBinding.tvStatus.visibility = View.INVISIBLE
    }

    private fun failedLoadMaps(string: String){
        activityEditMapsBinding.btnConfirm.visibility = View.INVISIBLE
        activityEditMapsBinding.map.visibility = View.INVISIBLE
        activityEditMapsBinding.pbMaps.visibility = View.INVISIBLE
        activityEditMapsBinding.tvStatus.visibility = View.VISIBLE
        activityEditMapsBinding.tvStatus.text = string
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapClickListener(this)

        // Add a marker in Sydney and move the camera
        val pinMaps = LatLng(mosqueData.latitude!!, mosqueData.longitude!!)
        mMap.addMarker(MarkerOptions().position(pinMaps).title(mosqueData.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pinMaps, DEFAULT_ZOOM))

        showMaps()
    }

    override fun onMapClick(p0: LatLng?) {

        mMap.clear()

        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(p0!!.latitude, p0.longitude, 1)
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            mMap?.addMarker(MarkerOptions().position(p0))

            mosqueData = MosqueEntity(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                addresses[0].getAddressLine(0),
                addresses[0].locality,
                addresses[0].subLocality,
                addresses[0].subAdminArea,
                addresses[0].adminArea,
                addresses[0].countryName,
                addresses[0].postalCode,
                p0.latitude,
                p0.longitude,
                ""
            )

            EditDataMosqueActivity.mosqueData = mosqueData

            activityEditMapsBinding.btnConfirm.visibility = View.VISIBLE
            activityEditMapsBinding.pbMaps.visibility = View.INVISIBLE

        } catch (e: Exception){

            Snackbar.make(activityEditMapsBinding.root, R.string.gagal_mendapatkan_lokasi, Snackbar.LENGTH_SHORT)
                .show()

            activityEditMapsBinding.btnConfirm.visibility = View.INVISIBLE
            activityEditMapsBinding.pbMaps.visibility = View.INVISIBLE

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_confirm -> {
                EditDataMosqueActivity.statusMaps = true
                finish()
            }
        }
    }
}