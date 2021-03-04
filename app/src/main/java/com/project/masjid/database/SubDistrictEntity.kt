package com.project.masjid.database

import android.os.Parcel
import android.os.Parcelable

data class SubDistrictEntity(
    var subDistrictName : String? = ""
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(subDistrictName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubDistrictEntity> {
        override fun createFromParcel(parcel: Parcel): SubDistrictEntity {
            return SubDistrictEntity(parcel)
        }

        override fun newArray(size: Int): Array<SubDistrictEntity?> {
            return arrayOfNulls(size)
        }
    }
}