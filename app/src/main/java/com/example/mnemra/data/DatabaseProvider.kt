package com.example.mnemra.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: MnemraDatabase? = null

    fun getDatabase(context: Context): MnemraDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MnemraDatabase::class.java,
                "mnemra_db"
            ).allowMainThreadQueries()
            .build()
            INSTANCE = instance
            instance
        }
    }
}