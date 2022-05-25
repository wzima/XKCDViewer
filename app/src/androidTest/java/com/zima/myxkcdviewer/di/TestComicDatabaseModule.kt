package com.zima.myxkcdviewer.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zima.myxkcdviewer.data.database.AppDatabase
import com.zima.myxkcdviewer.data.logic.dao.ComicDataDao
import com.zima.myxkcdviewer.data.logic.repository.ComicRoomRepository
import com.zima.myxkcdviewer.data.logic.utils.APIService
import com.zima.myxkcdviewer.data.models.Comic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

/**
 * This is the backend. The database.
 */


@InstallIn(SingletonComponent::class)
@Module
object TestComicDatabaseModule {
    @Provides
    fun provideLogDao(database: AppDatabase): ComicDataDao {
        return database.comicDataDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext,
            AppDatabase::class.java).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideRepository(localDataSource: ComicDataDao) =
        ComicRoomRepository(localDataSource)

    @Singleton
    @Provides
    fun provideAPIService(@ApplicationContext appContext: Context) =
        APIService(appContext)

}