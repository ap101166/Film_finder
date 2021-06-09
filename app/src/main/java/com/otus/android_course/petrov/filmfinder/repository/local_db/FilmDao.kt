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
}
