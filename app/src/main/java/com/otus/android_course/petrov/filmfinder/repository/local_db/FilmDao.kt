package com.otus.android_course.petrov.filmfinder.repository.local_db

import androidx.room.*

@Dao
interface IFilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favFilm: FavoriteFilm?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmList(films: List<Film>?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavorite(favFilm: FavoriteFilm?)

    @Delete
    fun deleteFavorite(favFilm: FavoriteFilm?)

    @Query("SELECT * FROM fav_films")
    fun getFavorites(): List<FavoriteFilm>

    @Query("SELECT * FROM all_films")
    fun getFilmList(): List<Film>

//    @Query("SELECT * FROM FavoriteFilm WHERE id = :id")
//    fun getById(id: Long): FavoriteFilm?

//    @Query("SELECT * FROM Publisher WHERE name LIKE :search ")
//    fun findPublishersWithName(search: String?): List<Publisher?>?
//
//    @Query("SELECT * FROM Publisher WHERE annual_revenue > :revenue ")
//    fun findPublisherWithRevenueMore(revenue: Int): List<Publisher?>?

}

@Dao
abstract class FilmDao : IFilmDao {

    @Transaction
    open fun insertAndDeleteInTransaction( // todo ????
        newPublisher: FavoriteFilm?,
        oldPublisher: FavoriteFilm?
    ) { // Anything inside this method runs in a single transaction.
        insertFavorite(newPublisher)
        deleteFavorite(oldPublisher)
    }

}
