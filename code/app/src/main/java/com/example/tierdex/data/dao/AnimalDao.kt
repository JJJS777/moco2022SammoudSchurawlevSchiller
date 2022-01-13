package com.example.tierdex.data.dao

import androidx.room.*
import com.example.tierdex.data.entities.Animals
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {

    @Query("SELECT * from ANIMAL ORDER BY animalID ASC")
    fun getAnimals(): Flow<List<Animals>>

    @Query("SELECT * from ANIMAL WHERE animalID = :animalID")
    fun getAnimal(animalID: Int): Flow<Animals>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert( animal: Animals )

    @Update
    suspend fun update( animal: Animals )

    @Delete
    suspend fun delete( animal: Animals )



}