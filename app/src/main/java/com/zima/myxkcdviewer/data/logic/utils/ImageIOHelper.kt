package com.zima.myxkcdviewer.data.logic.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.zima.myxkcdviewer.data.models.Comic
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class ImageIOHelper {

    //several helper methods to read/write bitmaps from the internal storage
    companion object {

        private fun getTempImagePath(context: Context): String? {
            // Get the context wrapper instance
            val wrapper = ContextWrapper(context)

            // Initializing a new file
            // The bellow line return a directory in internal storage
            val dir = wrapper.getDir("images", Context.MODE_PRIVATE)

            // Create a file to save the image
            val file = File(dir, Comic.TEMP_FILENAME + ".jpg")
            val uri = Uri.parse(file.absolutePath)
            return uri.encodedPath
        }

        fun getBitmap(path: String): Bitmap? {
            //get a bitmap from a uri
            val uri = Uri.parse(Uri.decode(path))
            return BitmapFactory.decodeFile(uri.encodedPath)
        }

        fun setImage(path: String, imageView: ImageView) {
            //clear content of imageview
            val uri = Uri.parse(Uri.decode(path))
            imageView.setImageURI(null)
            //set new uri for imageview
            imageView.setImageURI(uri)
        }



        private fun moveFile(fromPath: String, toPath: String) {
            File(fromPath).let { sourceFile ->
                sourceFile.copyTo(File(toPath), true)
                sourceFile.delete()
            }
        }

        //move the stored image to the temp path and also update the path in the comic object
        fun moveImageToTempPath(context: Context, comic: Comic) {

            try {
                val tempPath = getTempImagePath(context)
                tempPath?.let { myTempPath ->
                    comic.bitmapPath?.let { bitmapPath ->
                        moveFile(bitmapPath, myTempPath)
                        comic.bitmapPath = myTempPath
                    }
                }
            } catch (e: FileSystemException) {
                Log.e("moveImageToTempPath", "Could not move file")
            }

        }


        fun copyToUniqueUri(context: Context, currentPath: String): String? {
            try {
                val currentUri = Uri.parse(Uri.decode(currentPath))

                val fileCurrent = File(currentUri.encodedPath!!)
                val wrapper = ContextWrapper(context)

                var fileTarget = wrapper.getDir("images", Context.MODE_PRIVATE)
                fileTarget = File(fileTarget, "${UUID.randomUUID()}.jpg")

                fileCurrent.copyTo(fileTarget, true)

                return fileTarget.absolutePath
            } catch (e: Exception) {
                Log.e("copyToUniqueUri", "Could not copy file")
                return null
            }
        }


        // Method to save an image to internal storage
        private fun saveImageToInternalStorage(bitmap: Bitmap?, file: File): String? {
            if (file.exists())
                file.delete()

            val uri = Uri.parse(file.absolutePath)

            try {
                // Get the file output stream
                val stream: OutputStream = FileOutputStream(file)

                // Compress bitmap
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                // Flush the stream
                stream.flush()

                // Close stream
                stream.close()
            } catch (e: IOException) { // Catch the exception
                e.printStackTrace()
            }

            // Return the saved image uri
            return uri.encodedPath
        }


        // Method to save an image to internal storage
        fun saveImageToInternalStorage(context: Context, bitmap: Bitmap?, fileName: String): String? {

            try {// Get the context wrapper instance
                val wrapper = ContextWrapper(context)

                // Initializing a new file
                // The bellow line return a directory in internal storage
                val dir = wrapper.getDir("images", Context.MODE_PRIVATE)

                // Create a file to save the image
                val file = File(dir, "$fileName.jpg")

                return saveImageToInternalStorage(bitmap, file)
            } catch (e: Exception) {
                return null
            }

        }
    }
}