package com.zima.myxkcdviewer.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.zima.myxkcdviewer.R
import com.zima.myxkcdviewer.data.models.Comic
import com.zima.myxkcdviewer.data.logic.utils.ImageIOHelper
import com.zima.myxkcdviewer.data.logic.utils.ImageSaver
import com.zima.myxkcdviewer.databinding.ComicViewBinding
import com.zima.myxkcdviewer.ui.utils.MySnackbar
import com.zima.myxkcdviewer.ui.utils.MyTextToSpeechHelper
import com.zima.myxkcdviewer.ui.fragments.TipOfTheDayDialogFragment

class ComicView : ConstraintLayout {

    private var binding = ComicViewBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    var comic: Comic? = null
    var myTextToSpeechHelper: MyTextToSpeechHelper? = null
    var isFavorite = false
        set(value) {
            field = value
            binding.toolsView.setIsFavorite(value)

        }
    var onRefreshClickedListener: OnRefreshClickedListener? = null

    init {
        myTextToSpeechHelper = MyTextToSpeechHelper(context, binding.toolsView)
    }

    fun setOnFavoriteToggleListener(block: () -> Unit) {
        binding.toolsView.setOnClickListenerFavorites(block)
    }

    fun setClickListeners(fm: FragmentManager) {
        binding.ivComic.setOnPhotoTapListener { view, x, y ->
            TipOfTheDayDialogFragment.newInstance(R.string.Info, R.string.ImageZoomTip, TipOfTheDayDialogFragment.IMAGE_VIEW_TIP).isShow(
                fm, TipOfTheDayDialogFragment.IMAGE_VIEW_TIP,
                context, TipOfTheDayDialogFragment.IMAGE_VIEW_TIP
            )
        }

        binding.ivComic.setOnLongClickListener {
            showAltTextSnack()
            true
        }

        binding.toolsView.setOnClickListenerReadLoud {
            myTextToSpeechHelper?.readText(comic)
        }


        binding.itbRefresh.setOnClickListener {
            onRefreshClickedListener?.onRefreshClicked()
        }
    }


    fun setOnClickListenerSave(block: () -> Unit) {
        binding.toolsView.setOnClickListenerSave {
            block()
        }
    }

    fun getImageSaveAction() {
        comic?.bitmapPath?.let { bitmapURI ->
            comic?.id?.let { id ->
                ImageIOHelper.getBitmap(bitmapURI)?.let {
                    ImageSaver.saveMediaToStorage(context, binding.root, it, id)
                }
            }
        }

    }

    //show the alternative text of the comic as snackbar
    private fun showAltTextSnack() = comic?.let {
        MySnackbar.showDependentOnTextLength(binding.root, it.altText)
    }

    fun updateUI() {
        if (comic != null && comic?.bitmapPath == null) {
            return
        }

        binding.itbRefresh.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.toolsView.visibility = View.VISIBLE

        binding.tvTitle.text = comic?.title

        comic?.bitmapPath?.let {
            ImageIOHelper.setImage(it, binding.ivComic)
        }

        comic?.transcript?.let {
            myTextToSpeechHelper?.text = it
        }

    }


    fun showLoadingUI() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun showFailureUI(errorDescriptionResId: Int) {
        if (comic == null) {
            binding.itbRefresh.visibility = View.VISIBLE
            binding.toolsView.visibility = View.GONE
        }
        binding.progressBar.visibility = View.GONE
        MySnackbar.showShort(context, binding.root, errorDescriptionResId)
    }

    interface OnRefreshClickedListener {
        fun onRefreshClicked()
    }
}