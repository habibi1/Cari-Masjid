package com.project.masjid.ui.kiblat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.*
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.project.masjid.R
import com.project.masjid.databinding.ActivityArahKiblatBinding
import kotlin.math.roundToInt

class ArahKiblatActivity : AppCompatActivity() {

    private lateinit var activityArahKiblatBinding : ActivityArahKiblatBinding
    private lateinit var sensorManager : SensorManager
    private lateinit var sensor : Sensor
    private lateinit var animation: RotateAnimation
    private lateinit var userLocation: Location
    private var currentDegree : Float = 0.0f
    private var currentNeedleDegree: Float = 0.0f
    private var locationPermissionGranted = false

    companion object {
        private val TAG = ArahKiblatActivity::class.java.simpleName
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        const val QIBLA_LATITUDE = 21.42276
        const val QIBLA_LONGITUDE = 39.82624
        const val PIVOT = 0.5f
        const val DURATION = 200
        const val MAX_DEGREE = 360
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityArahKiblatBinding = ActivityArahKiblatBinding.inflate(layoutInflater)
        setContentView(activityArahKiblatBinding.root)

        getLocationPermission()
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
        getLocationPermission()
    }

    private fun getLocationPermission() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener {
                Log.d(TAG, "User Location : Lat : ${it.latitude} Long : ${it.longitude}")
                qiblaDirection(it.latitude, it.longitude)
            }
            fusedLocationClient.lastLocation.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun qiblaDirection(latitude: Double, longitude: Double) {
        userLocation = Location("User Location")
        userLocation.latitude = latitude
        userLocation.longitude = longitude

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        sensorManager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                val degree: Float = sensorEvent?.values?.get(0)?.roundToInt()?.toFloat()!!
                var head: Float = sensorEvent.values?.get(0)?.roundToInt()?.toFloat()!!

                val location = Location(getString(R.string.arah_kiblat))
                location.latitude = QIBLA_LATITUDE
                location.longitude = QIBLA_LONGITUDE

                var bearTo = userLocation.bearingTo(location)

                val geomagneticField = GeomagneticField(
                    userLocation.latitude.toFloat(),
                    userLocation.longitude.toFloat(),
                    userLocation.altitude.toFloat(),
                    System.currentTimeMillis()
                )

                head -= geomagneticField.declination

                if (bearTo < 0) {
                    bearTo += MAX_DEGREE
                }

                var direction = bearTo - head

                if (direction < 0) {
                    direction += MAX_DEGREE
                }

                animation = RotateAnimation(
                    currentNeedleDegree,
                    direction,
                    Animation.RELATIVE_TO_SELF,
                    PIVOT,
                    Animation.RELATIVE_TO_SELF,
                    PIVOT
                )
                animation.fillAfter = true
                animation.duration = DURATION.toLong()
                activityArahKiblatBinding.ivKiblat.startAnimation(animation)

                currentNeedleDegree = direction
                currentDegree = -degree

            }
        }, sensor, SensorManager.SENSOR_DELAY_GAME)
    }
}