package com.example.baism_cw_02.data

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlin.concurrent.Volatile

@Database(entities = [Person::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val personDao: PersonDao


    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,
                    "persons.db",
                )
                    .setDriver(BundledSQLiteDriver())
                    .build()
                    .also { instance = it }
            }
    }
}