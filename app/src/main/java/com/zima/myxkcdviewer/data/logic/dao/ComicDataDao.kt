package com.zima.myxkcdviewer.data.logic.dao

import androidx.room.*
import com.zima.myxkcdviewer.data.models.Comic
import kotlinx.coroutines.flow.Flow

//interface for interaction with the room database

@Dao
interface ComicDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComic(comic: Comic)

    @Update
    suspend fun updateComic(comic: Comic)

    @Delete
    suspend fun deleteComic(comic: Comic)

    @Query("SELECT * FROM Comic WHERE id = :id")
    fun getComicById(id: Int): List<Comic>?

    @Query("SELECT * FROM Comic ORDER BY id DESC")
    fun getComics(): Flow<List<Comic>>

    @Query("SELECT EXISTS(SELECT * FROM Comic WHERE id = :id)")
    fun exists(id: Int): Boolean
}