package com.example.tierdex.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

class Coordinates(var latitude: String, var longitude: String) { }

@Entity
data class Discovered (
    @PrimaryKey(autoGenerate = true)
    val discoID: Int = 0,
    @ColumnInfo(name = "name")
    val animalName: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @Embedded
    val coordinates: Coordinates?,
    @ColumnInfo(name = "picture")
    val camPicture: String?,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "postcode")
    val postcode: String?,
    val userID : String?,
    val imageUrl : String?
)


