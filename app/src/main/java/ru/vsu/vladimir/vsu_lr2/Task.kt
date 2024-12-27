package ru.vsu.vladimir.vsu_lr2

import android.os.Parcel
import android.os.Parcelable

data class Task(
    val description: String,
    val date: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable {

    constructor(parcel: Parcel) : this(
        description = parcel.readString() ?: "",
        date = parcel.readString() ?: "",
        latitude = parcel.readDouble(),
        longitude = parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Task> {
            override fun createFromParcel(parcel: Parcel): Task {
                return Task(parcel)
            }

            override fun newArray(size: Int): Array<Task?> {
                return arrayOfNulls(size)
            }
        }
    }
}



