package com.example.tierdex.data.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "discovered",
    //indices = [Index("animalID"), Index("userID")],
    foreignKeys = [
        ForeignKey(
            entity = Animals::class,
            parentColumns = ["animalID"],
            childColumns = ["tbl_animalID"]) ,
        ForeignKey(
            entity = Users::class,
            parentColumns = ["userID"],
            childColumns = ["tbl_userID"])
    ]
)
data class Discovered (
    @PrimaryKey()
    val tbl_animalID: Int,
    val tbl_userID: Int,
    @ColumnInfo(name = "discoverytime")
    val discoverytime: Int,
    @ColumnInfo(name = "location")
    val gps_location: Int,
    @ColumnInfo(name = "picture")
    val picture: Int,
    @ColumnInfo(name = "description")
    val description: Int,
    @ColumnInfo(name = "is_alive")
    val is_alive: Boolean
    ) { }
