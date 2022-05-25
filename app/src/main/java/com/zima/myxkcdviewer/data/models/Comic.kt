package com.zima.myxkcdviewer.data.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zima.myxkcdviewer.data.logic.utils.CalendarDayFormatter
import com.zima.myxkcdviewer.data.logic.utils.ImageIOHelper
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity

data class Comic(
    var month: Int = 1,
    @PrimaryKey
    var id: Int = 0,
    var year: Int = 2000,
    var news: String = "1",
    var safeTitle: String = "",
    var transcript: String = "",
    var altText: String = "",
    var title: String = "",
    var day: Int = 1,
    var bitmapPath: String? = null
) : Parcelable {


    fun copyFrom(other: Comic) {
        month = other.month
        id = other.id
        year = other.year
        news = other.news
        safeTitle = other.safeTitle
        transcript = other.transcript
        altText = other.altText
        title = other.title
        day = other.day
        bitmapPath = other.bitmapPath
    }

    fun idAsString() = id.toString()

    fun niceDate() = CalendarDayFormatter(day, month, year).toString()

    fun moveImageToTempPath(context: Context) {
        ImageIOHelper.moveImageToTempPath(context, this)
    }

    fun copyToUniqueUri(context: Context): Comic {
        val newPath = bitmapPath?.let { ImageIOHelper.copyToUniqueUri(context, it) }
        return copy().apply { bitmapPath = newPath }
    }

    companion object {
        const val TEMP_FILENAME = "comic_temp"

    }

}
