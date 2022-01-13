package com.example.tierdex.data.dao

import androidx.room.*
import com.example.tierdex.data.entities.Users

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert( user: Users )

    @Update
    suspend fun update( user: Users )

    @Delete
    suspend fun delete( user: Users )
}