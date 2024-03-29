package com.otus.android_course.petrov.filmfinder.repository.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteFilm::class, Film::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun filmDao(): IFilmDao
}

object Db {

    private var INSTANCE: AppDb? = null

    fun getInstance(context: Context): AppDb? {
        if (INSTANCE == null) {
            synchronized(AppDb::class) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDb::class.java, "film_finder_base.db"
                )
                    //.allowMainThreadQueries()
                    //.fallbackToDestructiveMigration()
                    //.addMigrations(MIGRATION_1_2)
                    //.addCallback(DbCallback())
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

//class DbCallback : RoomDatabase.Callback() {
//    override fun onCreate(db: SupportSQLiteDatabase) {
//        Log.d("asd123", "DbCallback onCreate")
//    }
//
//    override fun onOpen(db: SupportSQLiteDatabase) {
//        Log.d("asd123", "DbCallback onOpen")
//    }
//}
