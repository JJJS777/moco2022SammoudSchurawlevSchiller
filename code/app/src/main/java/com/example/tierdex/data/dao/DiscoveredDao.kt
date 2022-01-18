package com.example.tierdex.data.dao

import androidx.room.*
import com.example.tierdex.data.entities.Discovered
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscoveredDao {

//    @Query( "SELECT * FROM discovered ORDER BY tbl_animalID ASC")
//    fun getdiscoveries( ): Flow<List<Discovered>>

//    @Query( "SELECT * FROM discovered WHERE tbl_animalID = :animalID" )
//    fun getdiscovery( animalID: Int ): Flow<Discovered>

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    suspend fun insert ( discovery : Discovered )

    @Update
    suspend fun upadete ( discovery: Discovered )

    @Delete
    suspend fun delete ( discovery: Discovered )


}
