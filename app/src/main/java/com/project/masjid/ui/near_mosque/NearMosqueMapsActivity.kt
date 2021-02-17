package com.project.masjid.ui.near_mosque

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.masjid.R

class NearMosqueMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_mosque_maps)
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
        map = googleMap

        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[NearMosqueViewModel::class.java]
        val courses = viewModel.getMosque()

        val zoomLevel = 15f

        for (item in courses){
            val latitude = item.lat
            val longitude = item.lng
            val title = item.name

            val itemMosque = LatLng(latitude, longitude)
            map.addMarker(MarkerOptions().position(itemMosque).title(title))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(itemMosque, zoomLevel))
        }

    }
}