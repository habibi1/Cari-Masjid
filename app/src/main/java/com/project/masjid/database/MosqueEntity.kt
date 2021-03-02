package com.project.masjid.database

import android.os.Parcel
import android.os.Parcelable

data class MosqueEntity (
        var name : String?,
        var description : String?,
        var fasilitas : String?,
        var kegiatan : String?,
        var infoKotakAmal : String?,
        var sejarah : String?,
        val addresses : String?,
        val subDistrict : String?,
        val nameSubDistrict : String?,
        val district : String?,
        val province : String?,
        val country : String?,
        val postalCode : String?,
        val latitude : Double?,
        val Longitude : Double?,
        var downloadImage : String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(fasilitas)
        parcel.writeString(kegiatan)
        parcel.writeString(infoKotakAmal)
        parcel.writeString(sejarah)
        parcel.writeString(addresses)
        parcel.writeString(subDistrict)
        parcel.writeString(nameSubDistrict)
        parcel.writeString(district)
        parcel.writeString(province)
        parcel.writeString(country)
        parcel.writeString(postalCode)
        parcel.writeValue(latitude)
        parcel.writeValue(Longitude)
        parcel.writeString(downloadImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MosqueEntity> {
        override fun createFromParcel(parcel: Parcel): MosqueEntity {
            return MosqueEntity(parcel)
        }

        override fun newArray(size: Int): Array<MosqueEntity?> {
            return arrayOfNulls(size)
        }
    }
}