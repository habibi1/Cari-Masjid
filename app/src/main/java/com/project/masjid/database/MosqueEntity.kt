package com.project.masjid.database

data class MosqueEntity (
    var mosqueId: String,
    var name: String,
    var description: String,
    var lat: Double,
    var lng: Double,
    var district: String,
    var sub_district: String
)