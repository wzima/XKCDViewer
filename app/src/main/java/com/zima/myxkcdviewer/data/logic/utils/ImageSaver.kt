package com.zima.myxkcdviewer.data.logic.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.zima.myxkcdviewer.R
import com.zima.myxkcdviewer.ui.utils.MySnackbar
import java.io.*


object ImageSaver {

    //    save a bitmap to the local picture gallery of the device as JPEG
    fun saveMediaToStorage(context: Context, parentView: View, bitmap: Bitmap, id: Int) {
        // Generating a file name
        val filename = "xkcd$id.jpg"

        try {
            when {
                // For devices running android >= Q
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    // getting the contentResolver
                    context.contentResolver?.also { resolver ->

                        // Content resolver will process the contentvalues
                        val contentValues = ContentValues().apply {

                            // putting file information in content values
                            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                        }

                        // Inserting the contentValues to
                        // contentResolver and getting the Uri
                        val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                        // Opening an outputstream with the Uri that we got
                        val fos = imageUri?.let { resolver.openOutputStream(it) }
                        fos?.use {
                            // Finally writing the bitmap to the output stream that we opened
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                            MySnackbar.showShort(context, parentView, R.string.SavedInGallery)

                        }
                    }
                }
                else -> {
                    // These for devices running on android < Q
                    val imagesDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val file = File(imagesDir, filename)
                    try {
                        // Make sure the Pictures directory exists.
                        imagesDir.mkdirs()

                        val fos = FileOutputStream(file)
                        fos.use {
                            // Finally writing the bitmap to the output stream that we opened
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                            MySnackbar.showShort(context, parentView, com.zima.myxkcdviewer.R.string.SavedInGallery)
                            // Tell the media scanner about the new file so that it is
                            // immediately available to the user.
                            MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null,
                                OnScanCompletedListener { path, uri ->
                                })

                        }
                    } catch (e: IOException) {
                        // Unable to create file, likely because external storage is
                        // not currently mounted.
                        Log.w("ExternalStorage", "Error writing $file", e)
                    }
                }
            }

        } catch (e: Exception) {
            MySnackbar.showShort(context, parentView, R.string.SaveError)

        }
    }

}