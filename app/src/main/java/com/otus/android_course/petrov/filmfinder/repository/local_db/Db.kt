package com.otus.android_course.petrov.filmfinder.repository.local_db

import android.content.Context
import androidx.room.Room

object Db {

    private var INSTANCE: AppDb? = null

    fun getInstance(context: Context): AppDb? {
        if (INSTANCE == null) {
            synchronized(AppDb::class) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDb::class.java, "film_finder.db"
                )
       //           .allowMainThreadQueries()
       //             .fallbackToDestructiveMigration()
       //             .addMigrations(MIGRATION_1_2)
       //             .addCallback(DbCallback(context))
                    .build()
            }
        }
        return INSTANCE
    }

    fun destroyInstance() {
        INSTANCE?.close()
        INSTANCE = null
    }
}
