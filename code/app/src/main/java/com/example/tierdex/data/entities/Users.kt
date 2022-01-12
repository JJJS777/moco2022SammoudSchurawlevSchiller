package com.example.tierdex.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "users" )
data class Users (
    @PrimaryKey(autoGenerate = true)
    val userID: Int = 0,
    @ColumnInfo(name = "Name")
    val userName: String,
    @ColumnInfo(name = "Adresse")
    val userAdresse: String
    ) {}