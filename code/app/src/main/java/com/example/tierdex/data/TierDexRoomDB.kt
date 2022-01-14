package com.example.tierdex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tierdex.data.dao.AnimalDao
import com.example.tierdex.data.dao.UserDao
import com.example.tierdex.data.entities.Animals
import com.example.tierdex.data.entities.Discovered
import com.example.tierdex.data.entities.Users

@Database(entities = [Animals::class, Users::class], version = 1, exportSchema = false )
abstract class TierDexRoomDB: RoomDatabase() {
    abstract fun animalDao(): AnimalDao
    abstract fun userDao(): UserDao

    // allows access to the methods for creating or getting the database using the class name as the qualifier.
    companion object {

        //INSTANCE variable will keep a reference to the database
        @Volatile //alue of a volatile variable will never be cached, and all writes and reads will be done to and from the main memory
        private var INSTANCE: TierDexRoomDB? = null

        fun getDatabase(context: Context): TierDexRoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TierDexRoomDB::class.java,
                    "TierDexDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}




