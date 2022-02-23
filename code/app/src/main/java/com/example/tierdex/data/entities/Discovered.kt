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
    @Embedded
    val coordinates: Coordinates,
    @ColumnInfo(name = "picture")
    val camPicture: String
)


