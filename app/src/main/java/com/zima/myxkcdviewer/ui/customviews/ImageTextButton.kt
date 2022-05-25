package com.zima.myxkcdviewer.ui.customviews

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.zima.myxkcdviewer.R
import com.zima.myxkcdviewer.databinding.ImageTextButtonBinding

/**
 * TODO: document your custom view class.
 */
class ImageTextButton : ConstraintLayout {

    private var binding = ImageTextButtonBinding.inflate(LayoutInflater.from(context), this, true)
    private var drawableOn: Drawable? = null
    private var drawableOff: Drawable? = null
    private val colorFilter = PorterDuffColorFilter(resources.getColor(R.color.red, null), PorterDuff.Mode.MULTIPLY)

    fun setRed(isRed: Boolean) = if (isRed) {
        binding.ib.colorFilter = colorFilter
    } else {
        binding.ib.colorFilter = null
    }

    fun setTurnedOn(isTurnedOn: Boolean) = if (isTurnedOn)
        binding.ib.setImageDrawable(drawableOn)
    else
        binding.ib.setImageDrawable(drawableOff)


    //grey out reading icon when not available
    override fun setEnabled(enabled: Boolean) {
        binding.root.isEnabled = enabled
        binding.root.alpha = if (enabled) 1.0f else 0.3f
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }


    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton, defStyle, 0)

        val titleText = a.getString(R.styleable.ImageTextButton_titleText)
        val drawable = a.getDrawable(R.styleable.ImageTextButton_drawable)
        drawableOn = a.getDrawable(R.styleable.ImageTextButton_drawableOn)
        drawableOff = a.getDrawable(R.styleable.ImageTextButton_drawableOff)

        drawable?.let { binding.ib.setImageDrawable(it) } ?: binding.ib.setImageDrawable(drawableOn)
        binding.tvTitle.text = titleText


        a.recycle()

    }
}