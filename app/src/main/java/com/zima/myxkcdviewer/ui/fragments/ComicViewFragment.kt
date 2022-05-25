package com.zima.myxkcdviewer.ui.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zima.myxkcdviewer.R
import com.zima.myxkcdviewer.data.logic.utils.CalendarDayFormatter
import com.zima.myxkcdviewer.data.logic.utils.PermissionsHelper
import com.zima.myxkcdviewer.data.models.Comic
import com.zima.myxkcdviewer.databinding.FragmentComicViewBinding
import com.zima.myxkcdviewer.ui.ComicViewModel
import com.zima.myxkcdviewer.ui.utils.MyTextToSpeechHelper
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_COMIC_DATA = "ARG_COMIC_DATA"

@AndroidEntryPoint
open class ComicViewFragment : Fragment() {

    protected lateinit var binding: FragmentComicViewBinding
    private var actionBar: ActionBar? = null
    private var myTextToSpeechHelper: MyTextToSpeechHelper? = null

    //for a SDK Version < Q, we need to check the permissions for writing to the gallery. this is not needed for higher SDK versions
    private val saveImagePermissionsHelper = PermissionsHelper(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        binding.comicView.getImageSaveAction()
    }

    val comicViewModel: ComicViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { it ->
            val comic = it.getParcelable(ARG_COMIC_DATA) as? Comic
            comic?.let { myComic ->
                comicViewModel.setCurrentComic(myComic)
            }
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        actionBar = (activity as? AppCompatActivity)?.supportActionBar
        binding = FragmentComicViewBinding.inflate(inflater, container, false)

        myTextToSpeechHelper = binding.comicView.myTextToSpeechHelper

        binding.comicView.setClickListeners(parentFragmentManager)

        binding.comicView.setOnFavoriteToggleListener {
            comicViewModel.getCurrentComic().value?.let { myComic ->
                comicViewModel.exists(myComic) { exists ->
                    if (exists) {
                        comicViewModel.delete(requireContext(), myComic)
                    } else {
                        comicViewModel.insert(requireContext(), myComic)

                    }
                }
            }
        }


        binding.swipeToRefresh.isEnabled = false

        binding.comicView.setOnClickListenerSave {
            saveImagePermissionsHelper.start()
        }

        setObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialUIUpdate()

    }

    protected open fun initialUIUpdate() {
        updateUI(comicViewModel.getCurrentComic().value)
    }


    protected open fun setObservers() {
        comicViewModel.allFavoriteComics.observe(viewLifecycleOwner) {
            comicViewModel.getCurrentComic().value?.let { comic ->
                comicViewModel.exists(comic) { exists ->
                    binding.comicView.isFavorite = exists
                }
            }
        }
    }


    //constructs the title of the action bar
    private fun buildActionBarTitle(id: Int?): String {
        return id?.let { getString(R.string.xkcdID, it) } ?: getString(R.string.xkcd)
    }


    protected fun stopLoudReadingText() {
        myTextToSpeechHelper?.stopLoudReadingText()
    }


    override fun onPause() {
        super.onPause()
        stopLoudReadingText()
    }

    override fun onDestroy() {
        super.onDestroy()
        myTextToSpeechHelper?.shutdown()
    }

    protected open fun updateActionBar(comic: Comic?) {
        if (comic == null)
            return

        actionBar?.title = buildActionBarTitle(comic.id)
        val calendarDayFormatter = CalendarDayFormatter(comic.day, comic.month, comic.year)
        actionBar?.subtitle = calendarDayFormatter.toString()
    }

    //this updates the view of the current comic
    protected fun updateUI(comic: Comic?) {
        Log.d("ComicViewFragment", "updateUI")

        binding.comicView.comic = comic
        binding.comicView.updateUI()

        updateActionBar(comic)

    }


}