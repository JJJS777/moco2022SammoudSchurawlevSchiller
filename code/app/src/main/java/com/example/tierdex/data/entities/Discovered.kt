package com.example.tierdex.data.entities

import androidx.room.*

@Entity(
    tableName = "discovered",
    foreignKeys = [
        ForeignKey(
            entity = Animals::class,
            parentColumns = ["animalID"],
            childColumns = ["tbl_animalID"])
    ]
)
data class Discovered (
    @PrimaryKey(autoGenerate = true)
    val discoID: Int = 0,
    @ColumnInfo(index = true, name = "tbl_animalID")
    val tbl_animalID: Int,
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

data class DiscoveredAnimals (
    @Embedded val animal: Animals,
    @Relation(
        parentColumn = "animalID",
        entityColumn = "tbl_animalID"
    )
    val discoveries: List<Discovered>
    )


