package com.project.masjid.ui.search_mosque

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.project.masjid.R
import com.project.masjid.database.MosqueEntity
import com.project.masjid.databinding.ActivitySearchMapsBinding
import com.project.masjid.ui.near_mosque.NearMosqueMapsActivity
import java.util.*

class SearchMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var activitySearchMapsBinding: ActivitySearchMapsBinding


    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        private const val DEFAULT_ZOOM = 15f
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchMapsBinding = ActivitySearchMapsBinding.inflate(layoutInflater)
        setContentView(activitySearchMapsBinding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        activitySearchMapsBinding.topAppBar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getLocationPermission()
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

        getDeviceLocation()

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    // [START maps_current_place_get_device_location]
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            val latitude = intent.getDoubleExtra(getString(R.string.latitude), 0.0)
                            val longitude = intent.getDoubleExtra(getString(R.string.longitude), 0.0)
                            val title = intent.getStringExtra(getString(R.string.nama_masjid))

                            val loc2 = Location("")
                            loc2.latitude = latitude
                            loc2.longitude = longitude

                            val distance = lastKnownLocation!!.distanceTo(loc2)/1000

                            val itemMosque = LatLng(latitude, longitude)
                            mMap.addMarker(MarkerOptions().position(itemMosque).title(title).snippet("Jarak: " + String.format("%.1f", distance) + " KM"))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itemMosque, DEFAULT_ZOOM))
                        }
                    } else {

                        val latitude = intent.getDoubleExtra(getString(R.string.latitude), 0.0)
                        val longitude = intent.getDoubleExtra(getString(R.string.longitude), 0.0)
                        val title = intent.getStringExtra(getString(R.string.nama_masjid))

                        val itemMosque = LatLng(latitude, longitude)
                        mMap.addMarker(MarkerOptions().position(itemMosque).title(title))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itemMosque, DEFAULT_ZOOM))

                        Snackbar.make(activitySearchMapsBinding.root, R.string.gagal_hitung_jarak, Snackbar.LENGTH_SHORT)
                            .show()

                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    // [END maps_current_place_get_device_location]


    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    // [START maps_current_place_on_request_permissions_result]
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
    }
    // [END maps_current_place_on_request_permissions_result]

}