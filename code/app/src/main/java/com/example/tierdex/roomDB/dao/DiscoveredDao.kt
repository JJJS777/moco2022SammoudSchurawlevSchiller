package com.example.tierdex.roomDB.dao

import androidx.room.*
import com.example.tierdex.roomDB.entities.Discovered
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscoveredDao {

    @Query( "SELECT * FROM discovered ORDER BY discoID ASC")
    fun getDiscoveries( ): Flow<List<Discovered>>

    @Query( "SELECT * FROM discovered WHERE discoID = :discoID" )
    fun getDiscovery( discoID: Int ): Flow<Discovered>

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    suspend fun insert ( discovery : Discovered )

    @Update
    suspend fun upadete ( discovery: Discovered )

    @Delete
    suspend fun delete ( discovery: Discovered )


}
