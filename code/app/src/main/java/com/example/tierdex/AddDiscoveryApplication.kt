package com.example.tierdex

import android.app.Application
import com.example.tierdex.data.TierDexRoomDB

class AddDiscoveryApplication: Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database: TierDexRoomDB by lazy { TierDexRoomDB.getDatabase(this) }
}