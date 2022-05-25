package com.zima.myxkcdviewer.data.models

import android.content.Context
import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import com.zima.myxkcdviewer.data.logic.utils.ImageIOHelper

data class ComicGsonData(
    @SerializedName("month")
    val month: Int = 1,
    @SerializedName("num")
    val id: Int = 1,
    @SerializedName("link")
    val link: String = "",
    @SerializedName("year")
    val year: Int = 2000,
    @SerializedName("news")
    val news: String = "1",
    @SerializedName("safe_title")
    val safeTitle: String = "",
    @SerializedName("transcript")
    val transcript: String = "",
    @SerializedName("alt")
    val altText: String = "",
    @SerializedName("img")
    val imgURL: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("day")
    val day: Int = 1,

    ) {

    //copy the decoded gson to a new comic object
    //we dont save the bitmap in the Comic class, but save the image in the storage and put the path into the object
    fun copyToComicUsingBitmap(context: Context, bitmap: Bitmap?): Comic? {
        return bitmap?.let { myBitmap ->
            val uri = ImageIOHelper.saveImageToInternalStorage(context, myBitmap, Comic.TEMP_FILENAME)
            return uri?.let { Comic(month, id, year, news, safeTitle, transcript, altText, title, day, it) }
        }
    }

}