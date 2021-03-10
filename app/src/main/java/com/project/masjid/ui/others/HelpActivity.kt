package com.project.masjid.ui.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.masjid.R
import com.project.masjid.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var activityHelpBinding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHelpBinding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(activityHelpBinding.root)

        activityHelpBinding.topAppBar.setNavigationOnClickListener {
            this.onBackPressed()
        }
    }
}