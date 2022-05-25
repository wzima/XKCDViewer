package com.zima.myxkcdviewer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zima.myxkcdviewer.data.database.AppDatabase
import com.zima.myxkcdviewer.data.logic.dao.ComicDataDao
import com.zima.myxkcdviewer.data.di.ComicDatabaseModule
import com.zima.myxkcdviewer.data.models.Comic
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After

import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var userDao: ComicDataDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        userDao = db.comicDataDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runBlocking {
        val id = 1234
        val comic = Comic(2, 1234, 2022, "news", "The safe Title", "transcript", "alternative text", "The Title", 3)
        userDao.insertComic(comic)
        val myComic = userDao.getComicById(id)
        assertThat(myComic?.get(0), equalTo(comic))
    }
}
