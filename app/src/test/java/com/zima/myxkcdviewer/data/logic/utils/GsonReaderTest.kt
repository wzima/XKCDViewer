package com.zima.myxkcdviewer.data.logic.utils

import org.junit.Assert
import org.junit.Test

class GsonReaderTest {
    @Test
    fun `convert a json string to gson and comic, assert comic fields`() {
        val jsonString =
            "{\"month\": \"5\", \"num\": 2623, \"link\": \"\", \"year\": \"2022\", \"news\": \"\", \"safe_title\": \"Goofs\", \"transcript\": \"\", \"alt\": \"The film is set in 2018, but when Commander Bremberly chases the hologram through Times Square, there's a billboard for Avengers: Age of Ultron. Depending on the date, that billboard would have been advertising either Infinity War or this movie.\", \"img\": \"https://imgs.xkcd.com/comics/goofs.png\", \"title\": \"Goofs\", \"day\": \"23\"}"
        val comicGson = GsonReader.readFromJSON(jsonString)
        val comic = comicGson!!.copyToComicUsingUri("sampleuri")

        Assert.assertEquals("Goofs", comic.title)
        Assert.assertEquals(2022, comic.year)
    }
}