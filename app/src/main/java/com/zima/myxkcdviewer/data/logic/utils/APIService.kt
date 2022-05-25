package com.zima.myxkcdviewer.data.logic.utils

import android.R.attr.maxHeight
import android.R.attr.maxWidth
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.android.volley.*
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.zima.myxkcdviewer.interfaces.ReadDataListener
import com.zima.myxkcdviewer.interfaces.SuccessStatus
import com.zima.myxkcdviewer.ui.ComicViewModel


class APIService(private val context: Context) {
    //"https://xkcd.com/info.0.json"
    private val queue = Volley.newRequestQueue(context)


    //get the json from the given id. the urlBuilder creates the url-string
    //if not initialized, first get the id of today's comic and save it as maxid (via the readDataListener)
    //if successful, get the json of the given day and download the bitmap which is indicated in the json
    //for performance reasons, we do not want to save the bitmap in the database (favorites). we save the bitmap in the internal storage and only store the uri (or path) to it in the database

    fun fetchData(isInitialized: Boolean, id: Int, readDataListener: ReadDataListener?, urlBuilder: (Int) -> String) {
        readDataListener?.readingDataStarted()

        //if not yet initialized, get today's comic. via the readDataListener store the id as maximum allowed id in the viewmodel
        if (!isInitialized) {

            val jsonObjectRequestInitialize = JsonObjectRequest(
                Request.Method.GET, urlBuilder(ComicViewModel.ID_NOT_INITIALIZED), null,
                Response.Listener { jsonObject ->
                    val comicGson = GsonReader.readFromJSON(jsonObject.toString())

                    comicGson?.let {
                        readDataListener?.initializationFinished(SuccessStatus.Success, it.id)
                        fetchData(id, readDataListener, urlBuilder)
                    } ?: readDataListener?.readingDataFinished(SuccessStatus.JsonReadError, null)

                },
                getResponseErrorListener(readDataListener)
            )

            //add the jsonrequest to the queue
            queue.add(jsonObjectRequestInitialize)
        } else
            fetchData(id, readDataListener, urlBuilder)
    }


    //this is being called after successful initialization
    //first read the json and thereafter also get the image using the link in the json

    fun fetchData(id: Int, readDataListener: ReadDataListener?, urlBuilder: (Int) -> String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, urlBuilder(id), null,
            Response.Listener { jsonObject ->
                val comicGson = GsonReader.readFromJSON(jsonObject.toString())
                comicGson?.let { comicGsonData ->
                    comicGsonData.imgURL.let { url ->
                        //now download the image of the comic
                        val imageRequest = ImageRequest(
                            url, object : Response.Listener<Bitmap> {
                                override fun onResponse(bitmap: Bitmap?) {
                                    val comic = comicGsonData.copyToComicUsingBitmap(context, bitmap)

                                    comic?.let {
                                        readDataListener?.readingDataFinished(SuccessStatus.Success, it)
                                    } ?: readDataListener?.readingDataFinished(SuccessStatus.NoImage, null)

                                }
                            }, maxWidth, maxHeight, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565, object : Response.ErrorListener {
                                override fun onErrorResponse(error: VolleyError?) {
                                    when (error) {
                                        is NoConnectionError -> readDataListener?.readingDataFinished(SuccessStatus.NoConnection, null)
                                        else -> readDataListener?.readingDataFinished(SuccessStatus.NoImage, null)
                                    }
                                }
                            }
                        )

                        queue.add(imageRequest)
                    }
                } ?: readDataListener?.readingDataFinished(SuccessStatus.JsonReadError, null)
            },
            getResponseErrorListener(readDataListener)
        )

        queue.add(jsonObjectRequest)

    }


    private fun getResponseErrorListener(readDataListener: ReadDataListener?) = Response.ErrorListener { error ->
        when (error) {
            is NoConnectionError -> readDataListener?.readingDataFinished(SuccessStatus.NoConnection, null)
            is ClientError -> readDataListener?.readingDataFinished(SuccessStatus.UnknownComicId, null)
            else -> readDataListener?.readingDataFinished(SuccessStatus.UnknownError, null)
        }


    }

    fun cancelAll() {
        queue.cancelAll { true }

    }
}