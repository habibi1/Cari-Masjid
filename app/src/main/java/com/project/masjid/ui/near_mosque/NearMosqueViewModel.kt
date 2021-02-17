package com.project.masjid.ui.near_mosque

import androidx.lifecycle.ViewModel
import com.project.masjid.database.MosqueEntity
import com.project.masjid.utils.DataDummy

class NearMosqueViewModel: ViewModel() {
    fun getMosque(): List<MosqueEntity> = DataDummy.generateDummyMosque()
}