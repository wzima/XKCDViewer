package com.zima.myxkcdviewer.ui.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

object MySnackbar {
    fun showShort(context: Context, parentView: View?, textResId: Int) {
        parentView?.let { Snackbar.make(it, context.getString(textResId), Snackbar.LENGTH_SHORT).show() }
    }

    fun showLong(context: Context, parentView: View?, textResId: Int) {
        parentView?.let { Snackbar.make(it, context.getString(textResId), Snackbar.LENGTH_LONG).show() }
    }

    fun showShort(parentView: View?, text: String) {
        parentView?.let { Snackbar.make(it, text, Snackbar.LENGTH_SHORT).show() }
    }

    fun showLong(parentView: View?, text: String) {
        parentView?.let { Snackbar.make(it, text, Snackbar.LENGTH_LONG).show() }
    }

    fun showDependentOnTextLength(parentView: View?, text: String) {
        try {
            val numberOfWords = text.split(" ").size
            parentView?.let{Snackbar.make(it, text, numberOfWords * 300).show()}
        } catch (e: Exception) {
            showLong(parentView, text)
        }
    }

}