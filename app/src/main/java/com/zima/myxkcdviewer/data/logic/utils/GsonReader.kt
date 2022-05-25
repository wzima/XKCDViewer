package com.zima.myxkcdviewer.data.logic.utils

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.zima.myxkcdviewer.data.models.ComicGsonData


class GsonReader {
    companion object {
        fun readFromJSON(json: String): ComicGsonData? {
            return try {
                val gson = Gson()
                gson.fromJson(json, ComicGsonData::class.java)
            } catch (e: JsonParseException) {
                null
            }
        }
    }
}