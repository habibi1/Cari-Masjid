package com.project.masjid.utils

import com.project.masjid.database.MosqueEntity

object DataDummy {
    fun generateDummyMosque(): List<MosqueEntity>{

        val mosque = ArrayList<MosqueEntity>()

        mosque.add(MosqueEntity(
                "001",
                "Mushola BPK4",
                "Rajabasa, Kota Bandar Lampung, Lampung",
                -5.3677454743804764,
                105.21746753462739,
                "Kota Bandar Lampung",
                "Rajabasa",
        ))

        mosque.add(MosqueEntity(
                "002",
                "Coba",
                "Rajabasa, Kota Bandar Lampung, Lampung",
                -5.367555,
                105.215740,
                "Kota Bandar Lampung",
                "Rajabasa",
        ))

        return mosque
    }
}