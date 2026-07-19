package com.example.mnemra.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: MnemraDatabase? = null

    fun getDatabase(context: Context): MnemraDatabase =
        INSTANCE ?: synchronized(this) {

            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                MnemraDatabase::class.java,
                "mnemra.db"
            )
                .addMigrations(MnemraDatabase.MIGRATION_1_2)
                .build()
                .also {
                    INSTANCE = it
                }
        }
}