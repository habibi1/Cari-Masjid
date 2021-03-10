package com.project.masjid.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.project.masjid.R
import com.project.masjid.databinding.ActivityAdminBinding
import com.project.masjid.ui.admin.edit.EditMapsActivity
import com.project.masjid.ui.admin.edit.FilterDataMosqueActivity
import com.project.masjid.ui.admin.pin.PinPointMapsActivity

class AdminActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityAdminBinding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAdminBinding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(activityAdminBinding.root)

        activityAdminBinding.cvPinMaps.setOnClickListener(this)
        activityAdminBinding.cvEditMaps.setOnClickListener(this)

        activityAdminBinding.topAppBar.setNavigationOnClickListener {
            this.onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cv_pin_maps -> {
                startActivity(Intent(this, PinPointMapsActivity::class.java))
            }
            R.id.cv_edit_maps -> {
                startActivity(Intent(this, FilterDataMosqueActivity::class.java))
            }
        }
    }
}