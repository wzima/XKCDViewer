package com.zima.myxkcdviewer.data.logic.utils

import com.zima.myxkcdviewer.data.models.ComicJson
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {
    @GET("info.0.json")
    fun getTodaysComic(): Call<ComicJson>
}