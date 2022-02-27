package com.example.tierdex.roomDB

import android.app.Application
import com.example.tierdex.roomDB.TierDexRoomDB

class TierDexApplication: Application() {
    val database: TierDexRoomDB by lazy { TierDexRoomDB.getDatabase(this) }
}