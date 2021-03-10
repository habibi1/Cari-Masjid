package com.project.masjid.ui.others

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.masjid.R
import com.project.masjid.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var activityAboutBinding: ActivityAboutBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAboutBinding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(activityAboutBinding.root)

        activityAboutBinding.tvAppVersion.text = getString(R.string.versi) + " " + getAppVersion(this)

        activityAboutBinding.topAppBar.setNavigationOnClickListener{
            this.onBackPressed()
        }
    }

    private fun getAppVersion(context: Context): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }
}