package com.zima.myxkcdviewer.ui.customviews

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.zima.myxkcdviewer.R
import com.zima.myxkcdviewer.databinding.ToolsViewBinding

class ToolsView : ConstraintLayout {

    private var binding = ToolsViewBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    fun setOnClickListenerFavorites(block: () -> Unit) = binding.ibFavorites.setOnClickListener { block() }
    fun setOnClickListenerReadLoud(block: () -> Unit) = binding.ibReadLoud.setOnClickListener { block() }
    fun setOnClickListenerSave(block: () -> Unit) = binding.ibSave.setOnClickListener { block() }

    //grey out reading icon when not available
    fun enableReadLoud(enabled: Boolean) {
        binding.ibReadLoud.isEnabled = enabled
    }

    //color the reading icon red while reading loud
    fun isReadingLoud(isReading: Boolean) {
        binding.ibReadLoud.setRed(isReading)
    }

    fun setIsFavorite(isFavorite: Boolean) {
        binding.ibFavorites.setTurnedOn(isFavorite)
    }
}