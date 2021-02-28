package com.project.masjid.database

import android.os.Parcel
import android.os.Parcelable

data class MosqueEntity (
        val name : String?,
        val description : String?,
        val addresses : String?,
        val subDistrict : String?,
        val nameSubDistrict : String?,
        val district : String?,
        val province : String?,
        val country : String?,
        val postalCode : String?,
        val latitude : Double?,
        val Longitude : Double?
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
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(addresses)
        parcel.writeString(subDistrict)
        parcel.writeString(nameSubDistrict)
        parcel.writeString(district)
        parcel.writeString(province)
        parcel.writeString(country)
        parcel.writeString(postalCode)
        parcel.writeValue(latitude)
        parcel.writeValue(Longitude)
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