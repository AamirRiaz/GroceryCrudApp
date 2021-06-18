package com.example.grocerycrudsampleapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_list_table")
data class GroceryList(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "grocery_list_id")
    var id: Int,

    @ColumnInfo(name = "grocery_list_name")
    var name: String,

    @ColumnInfo(name = "grocery_list_status")
    var status: String

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroceryList> {
        override fun createFromParcel(parcel: Parcel): GroceryList {
            return GroceryList(parcel)
        }

        override fun newArray(size: Int): Array<GroceryList?> {
            return arrayOfNulls(size)
        }
    }
}