package com.zima.myxkcdviewer.interfaces

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.zima.myxkcdviewer.data.models.Comic
import java.util.concurrent.Flow

interface RepositoryInterface {
    fun getAllFavoriteComics(): kotlinx.coroutines.flow.Flow<List<Comic>>

    suspend fun insert(comic: Comic)

    suspend fun deleteComic(comic: Comic)

    suspend fun exists(comic: Comic): Boolean

    suspend fun getComicFromID(id: Int): Comic?

}