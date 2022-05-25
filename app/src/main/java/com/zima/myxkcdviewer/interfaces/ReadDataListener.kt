package com.zima.myxkcdviewer.interfaces

import com.zima.myxkcdviewer.data.models.Comic

//a listener to the api service to return the status and data of a get request
interface ReadDataListener {
    fun readingDataStarted()
    fun readingDataFinished(successStatus: SuccessStatus, comic: Comic?)
    fun initializationFinished(successStatus: SuccessStatus, maxId: Int)
}