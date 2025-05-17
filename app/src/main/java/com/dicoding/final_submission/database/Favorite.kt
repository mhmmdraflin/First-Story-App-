package com.dicoding.final_submission.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorites")
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "mediaCover")
    var mediaCover: String? = null,
) : Parcelable