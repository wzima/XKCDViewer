package com.zima.myxkcdviewer.data.logic.utils

import com.zima.myxkcdviewer.ui.ComicViewModel
import kotlin.random.Random


object ComicURLBuilder {

    const val BASE_URL = "https://xkcd.com"

    private fun getToday() = "https://xkcd.com/info.0.json"
    fun getFromId(id: Int) = if (id == ComicViewModel.ID_NOT_INITIALIZED) getToday() else "https://xkcd.com/$id/info.0.json"
    fun getRandomID(maxId: Int) = if (maxId == ComicViewModel.ID_NOT_INITIALIZED) getToday() else getFromId(Random(System.currentTimeMillis()).nextInt(1, maxId + 1))

}