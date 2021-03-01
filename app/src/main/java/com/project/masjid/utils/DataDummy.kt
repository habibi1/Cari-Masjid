package com.project.masjid.utils

import com.project.masjid.database.MosqueEntity

object DataDummy {
    fun generateDummyMosque(): List<MosqueEntity>{

        val mosque = ArrayList<MosqueEntity>()

        mosque.add(MosqueEntity(
                "Mushola BPK4",
                "Deskripsi",
                "Rajabasa, Kota Bandar Lampung, Lampung",
                "Kecamatan Rajabasa",
                "Rajabasa",
                "Kota Bandar Lampung",
                "Lampung",
                "Indonesia",
                "54565",
                -5.3677454743804764,
                105.21746753462739,
                ""
        ))

        mosque.add(MosqueEntity(
                "Mushola BPK",
                "Deskripsi",
                "Rajabasa, Kota Bandar Lampung, Lampung",
                "Kecamatan Rajabasa",
                "Rajabasa",
                "Kota Bandar Lampung",
                "Lampung",
                "Indonesia",
                "54565",
                -5.367555,
                105.215740,
                ""
        ))

        return mosque
    }
}