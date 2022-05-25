package com.zima.myxkcdviewer

import com.zima.myxkcdviewer.data.models.Comic
import com.zima.myxkcdviewer.interfaces.RepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : RepositoryInterface {

    private val comics = mutableListOf<Comic>()

    override fun getAllFavoriteComics(): Flow<List<Comic>> {
        return flow { emit(comics) }
    }

    override suspend fun insert(comic: Comic) {
        comics.add(comic)
    }

    override suspend fun deleteComic(comic: Comic) {
        comics.remove(comic)
    }

    override suspend fun exists(comic: Comic): Boolean {
        return getComicFromID(comic.id) != null
    }

    override suspend fun getComicFromID(id: Int): Comic? {
        return comics.find { it.id == id }
    }
}