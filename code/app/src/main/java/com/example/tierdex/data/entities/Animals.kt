package com.example.tierdex.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//This class will represent a database entity in our app
@Entity( tableName = "animal" )
data class Animals (
    @PrimaryKey(autoGenerate = true)
    val animalID: Int = 0,
    @ColumnInfo(name = "species")
    val animalSpecies: String,
    @ColumnInfo(name = "family")
    val animalFamily: String,
    @ColumnInfo(name = "order")
    val animalOrder: String,
    @ColumnInfo(name = "class")
    val animalClass: String
) { }