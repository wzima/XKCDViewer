package com.zima.myxkcdviewer.data.logic.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.zima.myxkcdviewer.data.logic.dao.ComicDataDao
import com.zima.myxkcdviewer.data.models.Comic
import com.zima.myxkcdviewer.interfaces.RepositoryInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
@Singleton
class ComicRoomRepository @Inject constructor(private val comicDataDao: ComicDataDao) : RepositoryInterface {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    override fun getAllFavoriteComics() = comicDataDao.getComics()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(comic: Comic) {
        comicDataDao.insertComic(comic)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun deleteComic(comic: Comic) {
        comicDataDao.deleteComic(comic)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun exists(comic: Comic) = comicDataDao.exists(comic.id)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun getComicFromID(id: Int): Comic? {
        return comicDataDao.getComicById(id)?.get(0)
    }

}