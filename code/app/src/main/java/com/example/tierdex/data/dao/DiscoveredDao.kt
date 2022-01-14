package com.example.tierdex.data.dao

import androidx.room.*
import com.example.tierdex.data.entities.Discovered
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscoveredDao {

    @Query( "SELECT * FROM discovered ORDER BY tbl_animal_id ASC")
    fun getdiscoveries( userID: Int ): Flow<List<Discovered>>

    @Query( "SELECT * FROM discovered WHERE tbl_animal_id = :animalID AND tbl_user_id = :userID" )
    fun getdiscovery( animalID: Int, userID: Int ): Flow<Discovered>

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    suspend fun insert ( discovery : Discovered )

    @Update
    suspend fun upadete ( discovery: Discovered )

    @Delete
    suspend fun delete ( discovery: Discovered )


}
