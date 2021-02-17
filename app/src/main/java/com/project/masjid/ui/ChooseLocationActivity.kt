package com.project.masjid.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.project.masjid.R
import com.project.masjid.databinding.ActivityChooseLocationBinding

class ChooseLocationActivity : AppCompatActivity() {

    private lateinit var activityChooseLocationBinding : ActivityChooseLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityChooseLocationBinding = ActivityChooseLocationBinding.inflate(layoutInflater)
        setContentView(activityChooseLocationBinding.root)

        val items = listOf("Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_list, items)
        (activityChooseLocationBinding.actPilihKabupaten as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun requireContext(): Context {
        return applicationContext
    }
}