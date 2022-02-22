package com.example.tierdex

import android.app.Application
import com.example.tierdex.data.TierDexRoomDB

class TierDexApplication: Application() {
    val database: TierDexRoomDB by lazy { TierDexRoomDB.getDatabase(this) }
}