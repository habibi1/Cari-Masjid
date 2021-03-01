package com.project.masjid.ui.admin.pin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.project.masjid.R
import com.project.masjid.database.MosqueEntity
import com.project.masjid.databinding.ActivityPinPointMapsBinding
import com.project.masjid.ui.near_mosque.NearMosqueMapsActivity
import java.util.*

class PinPointMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {

    private lateinit var map: GoogleMap
    private var cameraPosition: CameraPosition? = null

    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var activityPinPointMapsBinding: ActivityPinPointMapsBinding

    private lateinit var mosqueData : MosqueEntity

    companion object {
        private val TAG = PinPointMapsActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15f
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityPinPointMapsBinding = ActivityPinPointMapsBinding.inflate(layoutInflater)

        // [START_EXCLUDE silent]
        // Retrieve location and camera position from saved instance state.
        // [START maps_current_place_on_create_save_instance_state]
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        // [END maps_current_place_on_create_save_instance_state]
        // [END_EXCLUDE]

        setContentView(activityPinPointMapsBinding.root)

        hiddenMaps()

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        activityPinPointMapsBinding.btnConfirm.setOnClickListener(this)
    }

    private fun hiddenMaps(){
        activityPinPointMapsBinding.btnConfirm.visibility = View.INVISIBLE
        activityPinPointMapsBinding.map.visibility = View.INVISIBLE
        activityPinPointMapsBinding.pbMaps.visibility = View.VISIBLE
        activityPinPointMapsBinding.tvStatus.visibility = View.VISIBLE
        activityPinPointMapsBinding.tvStatus.text = getString(R.string.mencari_lokasi)
    }

    private fun showMaps(){
        activityPinPointMapsBinding.btnConfirm.visibility = View.INVISIBLE
        activityPinPointMapsBinding.map.visibility = View.VISIBLE
        activityPinPointMapsBinding.pbMaps.visibility = View.INVISIBLE
        activityPinPointMapsBinding.tvStatus.visibility = View.INVISIBLE
    }

    private fun failedLoadMaps(string: String){
        activityPinPointMapsBinding.btnConfirm.visibility = View.INVISIBLE
        activityPinPointMapsBinding.map.visibility = View.INVISIBLE
        activityPinPointMapsBinding.pbMaps.visibility = View.INVISIBLE
        activityPinPointMapsBinding.tvStatus.visibility = View.VISIBLE
        activityPinPointMapsBinding.tvStatus.text = string
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

        map.setOnMapClickListener(this)

        Snackbar.make(activityPinPointMapsBinding.root, R.string.pilih_lokasi_masjid, Snackbar.LENGTH_SHORT)
            .show()
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Prompt the user for permission.
        getLocationPermission()
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
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

                        showMaps()

                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {

                            val addresses: List<Address>
                            val geocoder = Geocoder(this, Locale.getDefault())

                            /*addresses = geocoder.getFromLocation(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            val address: String = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                            val city: String = addresses[0].locality
                            val state: String = addresses[0].adminArea
                            val country: String = addresses[0].countryName
                            val postalCode: String = addresses[0].postalCode
                            val knownName: String = addresses[0].featureName
                            val knownNae: String = addresses[0].subAdminArea
                            val knowjnNdae: String = addresses[0].subLocality
                            Log.d(TAG, city)
                            Log.d(TAG, state)
                            Log.d(TAG, country)
                            Log.d(TAG, postalCode)
                            Log.d(TAG, knownName)
                            Log.d(TAG, knownNae)
                            Log.d(TAG, knowjnNdae)
                            Log.d(TAG, address)
                            Log.d(TAG, addresses.toString())
*/
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    LatLng(lastKnownLocation!!.latitude,
                                            lastKnownLocation!!.longitude), DEFAULT_ZOOM))
                        }
                    } else {

                        failedLoadMaps(getString(R.string.lokasi_tidak_ditemukan))

                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        /*map?.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM))
                        map?.uiSettings?.isMyLocationButtonEnabled = false*/
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
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
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
        updateLocationUI()
    }
    // [END maps_current_place_on_request_permissions_result]

    /**
     * Saves the state of the map when the activity is paused.
     */
    // [START maps_current_place_on_save_instance_state]
    override fun onSaveInstanceState(outState: Bundle) {
        map.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map?.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }
    // [END maps_current_place_on_save_instance_state]

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    // [END maps_current_place_update_location_ui]

    override fun onMapClick(p0: LatLng?) {

        activityPinPointMapsBinding.pbMaps.visibility = View.VISIBLE

        map.clear()

        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(p0!!.latitude, p0.longitude, 1)
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            map?.addMarker(MarkerOptions().position(p0!!))

            mosqueData = MosqueEntity(
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

            activityPinPointMapsBinding.btnConfirm.visibility = View.VISIBLE
            activityPinPointMapsBinding.pbMaps.visibility = View.INVISIBLE
        } catch (e: Exception){

            Snackbar.make(activityPinPointMapsBinding.root, R.string.gagal_mendapatkan_lokasi, Snackbar.LENGTH_SHORT)
                .show()

            activityPinPointMapsBinding.pbMaps.visibility = View.INVISIBLE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_confirm -> {
                val moveWithObjectIntent = Intent(this, FormAddMosqueActivity::class.java)
                moveWithObjectIntent.putExtra(FormAddMosqueActivity.EXTRA_MOSQUE, mosqueData)
                startActivity(moveWithObjectIntent)
                finish()
            }
        }
    }

}