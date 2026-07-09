package com.example.mnemra.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: MnemraDatabase? = null

    fun getDatabase(context: Context): MnemraDatabase {

        return INSTANCE ?: synchronized(this) {

            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                MnemraDatabase::class.java,
                "mnemra.db"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}