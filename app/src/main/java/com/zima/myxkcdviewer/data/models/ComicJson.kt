package com.zima.myxkcdviewer.data.models

import android.content.Context
import android.graphics.Bitmap
import com.zima.myxkcdviewer.data.logic.utils.ImageIOHelper

data class ComicJson(
    val alt: String,
    val day: String,
    val img: String,
    val link: String,
    val month: String,
    val news: String,
    val num: Int,
    val safe_title: String,
    val title: String,
    val transcript: String,
    val year: String
) {
    //copy the decoded gson to a new comic object
    //we dont save the bitmap in the Comic class, but save the image in the storage and put the path into the object
    fun copyToComicUsingBitmap(context: Context, bitmap: Bitmap?): Comic? {
        return bitmap?.let { myBitmap ->
            val uri = ImageIOHelper.saveImageToInternalStorage(context, myBitmap, Comic.TEMP_FILENAME)
            return uri?.let { Comic(month.toInt(), num, year.toInt(), news, safe_title, transcript, alt, title, day.toInt(), it) }
        }
    }

}