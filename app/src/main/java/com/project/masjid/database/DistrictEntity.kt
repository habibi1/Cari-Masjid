package com.project.masjid.database

import android.os.Parcel
import android.os.Parcelable

data class DistrictEntity(
    var districtName: String? = ""
): Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(districtName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DistrictEntity> {
        override fun createFromParcel(parcel: Parcel): DistrictEntity {
            return DistrictEntity(parcel)
        }

        override fun newArray(size: Int): Array<DistrictEntity?> {
            return arrayOfNulls(size)
        }
    }
}