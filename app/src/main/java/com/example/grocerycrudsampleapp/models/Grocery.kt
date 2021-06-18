package com.example.grocerycrudsampleapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_table")
data class Grocery(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "grocery_id")
    var id: Int,

    @ColumnInfo(name = "grocery_list_id")
    var groceryListId: Int,

    @ColumnInfo(name = "grocery_name")
    var name: String,

    @ColumnInfo(name = "grocery_amount")
    var amount: String,

    @ColumnInfo(name = "grocery_status")
    var status: String,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(groceryListId)
        parcel.writeString(name)
        parcel.writeString(amount)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Grocery> {
        override fun createFromParcel(parcel: Parcel): Grocery {
            return Grocery(parcel)
        }

        override fun newArray(size: Int): Array<Grocery?> {
            return arrayOfNulls(size)
        }
    }
}