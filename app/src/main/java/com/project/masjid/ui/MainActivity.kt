package com.project.masjid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.project.masjid.R
import com.project.masjid.databinding.ActivityMainBinding
import com.project.masjid.ui.kiblat.ArahKiblatActivity
import com.project.masjid.ui.login.LoginActivity
import com.project.masjid.ui.near_mosque.NearMosqueMapsActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.btnMasjidTerdekat.setOnClickListener(this)
        activityMainBinding.btnCariMasjid.setOnClickListener(this)
        activityMainBinding.btnLogin.setOnClickListener(this)
        activityMainBinding.btnArahKiblat.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_masjid_terdekat -> {
                startActivity(Intent(this, NearMosqueMapsActivity::class.java))
            }
            R.id.btn_cari_masjid -> {
                startActivity(Intent(this, ChooseLocationActivity::class.java))
            }
            R.id.btn_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.btn_arah_kiblat -> {
                startActivity(Intent(this, ArahKiblatActivity::class.java))
            }
        }
    }
}