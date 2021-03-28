package com.otus.android_course.petrov.filmfinder.repository.local_db

import androidx.room.*
import com.otus.android_course.petrov.filmfinder.repository.local_db.entities.FavoriteFilms

@Dao
interface IFilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favFilm: FavoriteFilms?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(favFilm: FavoriteFilms?)

    @Delete
    fun delete(favFilm: FavoriteFilms?)

    @Query("SELECT * FROM FavoriteFilms")
    fun getAll(): List<FavoriteFilms?>?

//    @Query("SELECT * FROM Publisher WHERE id = :id")
//    fun getById(id: Long): Publisher?
//
//    @Query("SELECT * FROM Publisher WHERE name LIKE :search ")
//    fun findPublishersWithName(search: String?): List<Publisher?>?
//
//    @Query("SELECT * FROM Publisher WHERE annual_revenue > :revenue ")
//    fun findPublisherWithRevenueMore(revenue: Int): List<Publisher?>?

}

@Dao
abstract class FilmDao : IFilmDao {

    @Transaction
    open fun insertAndDeleteInTransaction(
        newPublisher: FavoriteFilms?,       // todo
        oldPublisher: FavoriteFilms?        //
    ) { // Anything inside this method runs in a single transaction.
        insert(newPublisher)
        delete(oldPublisher)
    }

}
