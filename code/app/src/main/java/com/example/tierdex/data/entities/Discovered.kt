package com.example.tierdex.data.entities

import androidx.room.*
import java.sql.Date

@Entity
data class Discovered (
    @PrimaryKey(autoGenerate = true)
    val discoID: Int = 0,
    @ColumnInfo(name = "name")
    val animalName: String,
)


