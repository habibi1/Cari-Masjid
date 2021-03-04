package com.project.masjid.ui.search_mosque

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.masjid.R
import com.project.masjid.ui.near_mosque.NearMosqueMapsActivity

class SearchMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    companion object {
        private const val DEFAULT_ZOOM = 15f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        val latitude = intent.getDoubleExtra(getString(R.string.latitude), 0.0)
        val longitude = intent.getDoubleExtra(getString(R.string.longitude), 0.0)
        val title = intent.getStringExtra(getString(R.string.nama_masjid))

        val itemMosque = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(itemMosque).title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itemMosque, DEFAULT_ZOOM))

    }
}