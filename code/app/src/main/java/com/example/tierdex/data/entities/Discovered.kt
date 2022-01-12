package com.example.tierdex.data.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "discovered",
    indices = [Index("animal_id"), Index("user_id")],
    foreignKeys = [
        ForeignKey(
            entity = Animals::class,
            parentColumns = ["animal_id"],
            childColumns = ["tbl_animal_id"]) ,
        ForeignKey(
            entity = Users::class,
            parentColumns = ["user_id"],
            childColumns = ["tbl_user_id"])
    ]
)

data class Discovered (
    @PrimaryKey()
    val tbl_animal_id: Int,
    val tbl_user_id: Int,
    @ColumnInfo(name = "discoverytime")
    val discoverytime: Date,
    @ColumnInfo(name = "location")
    val gps_location: Int,
    @ColumnInfo(name = "picture")
    val picture: Int,
    @ColumnInfo(name = "description")
    val description: Int,
    @ColumnInfo(name = "is_alive")
    val is_alive: Boolean
    ) { }