package com.zima.myxkcdviewer.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.zima.myxkcdviewer.R
import com.zima.myxkcdviewer.R.menu.mainmenu
import com.zima.myxkcdviewer.data.logic.utils.APIService
import com.zima.myxkcdviewer.data.logic.utils.ComicURLBuilder
import com.zima.myxkcdviewer.data.models.Comic
import com.zima.myxkcdviewer.interfaces.ReadDataListener
import com.zima.myxkcdviewer.interfaces.SuccessStatus
import com.zima.myxkcdviewer.ui.ComicViewModel
import com.zima.myxkcdviewer.ui.customviews.ComicView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ComicNavigationViewFragment : ComicViewFragment() {

    //create an instance of the apiservice. this is responsible to retrieve the json from the web
    @Inject
    lateinit var apiService: APIService
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        binding.comicView.onRefreshClickedListener = object : ComicView.OnRefreshClickedListener {
            override fun onRefreshClicked() {
                updateForId(ComicViewModel.ID_NOT_INITIALIZED)
            }

        }

        binding.swipeToRefresh.isEnabled = true
        binding.swipeToRefresh.setOnRefreshListener {
            updateForId(ComicViewModel.ID_NOT_INITIALIZED)
            binding.swipeToRefresh.isRefreshing = false
        }
        return view
    }


    override fun initialUIUpdate() {
        //initialize the viewmodel (determine the maximum=today's id) and show today's comic
        if (!comicViewModel.isInitialized)
            updateForId(ComicViewModel.ID_NOT_INITIALIZED)
    }

    override fun setObservers() {

        //set a listener on all comics in the database
        //this will toggle the favorite icon on or off
        comicViewModel.allFavoriteComics.observe(viewLifecycleOwner) {
            comicViewModel.getCurrentComic().value?.let { comic ->
                comicViewModel.exists(comic) { exists ->
                    binding.comicView.isFavorite = exists
                }
            }
        }

        //if the comic has been loaded, update the UI
        comicViewModel.getCurrentComic().observe(viewLifecycleOwner) { it ->
            updateUI(it)
            comicViewModel.exists(it) {
                binding.comicView.isFavorite = it
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(mainmenu, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        apiService.cancelAll()
        stopLoudReadingText()

        when (item.itemId) {
            R.id.random -> random()
            R.id.today -> today()
            R.id.previous -> previousId()
            R.id.next -> nextId()
            R.id.favorites -> {
                val navCtr = findNavController()
                navCtr.navigate(R.id.action_comicViewFragment_to_favoriteComicFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun today() {
        updateForId(ComicViewModel.ID_NOT_INITIALIZED)
    }

    private fun nextId() {
        if (comicViewModel.isInitialized)
            updateForId(comicViewModel.currentId + 1)
        else
            updateForId(ComicViewModel.ID_NOT_INITIALIZED)


    }

    private fun previousId() {
        if (comicViewModel.isInitialized)
            updateForId(comicViewModel.currentId - 1)
        else
            updateForId(ComicViewModel.ID_NOT_INITIALIZED)
    }

    //get a comic for a specific id
    //if id is ComicViewModel.ID_NOT_INITIALIZED, today's comic is loaded
    private fun updateForId(id: Int) = apiService.fetchData(comicViewModel.isInitialized, id, readJSONListener) { ComicURLBuilder.getFromId(it) }

    //get a comic with random id
    private fun random() = apiService.fetchData(comicViewModel.isInitialized, 0, readJSONListener) { ComicURLBuilder.getRandomID(comicViewModel.maxId) }


    //a listener for the apiservice
    private val readJSONListener = object : ReadDataListener {

        //get the maxId from today's comic
        override fun initializationFinished(successStatus: SuccessStatus, maxId: Int) {
            when (successStatus) {
                SuccessStatus.Success -> {
                    comicViewModel.maxId = maxId
                    comicViewModel.isInitialized = true

                }
                else -> {
                    comicViewModel.maxId = ComicViewModel.ID_NOT_INITIALIZED
                    comicViewModel.isInitialized = false
                    binding.comicView.showFailureUI(successStatus.descriptionResId)
                }
            }
        }

        override fun readingDataStarted() {
            //show the progress bar
            binding.comicView.showLoadingUI()
        }

        override fun readingDataFinished(successStatus: SuccessStatus, comic: Comic?) {
            //when the download has been successful, set the comic as current comic in the viewmodel
            //otherwise keep the id of the previous comic and show an error message
            when (successStatus) {
                SuccessStatus.Success ->
                    comic?.let {
                        comicViewModel.setCurrentComic(it)
                    }
                else -> {
                    comicViewModel.currentId = comicViewModel.getCurrentComic().value?.id ?: ComicViewModel.ID_NOT_INITIALIZED
                    binding.comicView.showFailureUI(successStatus.descriptionResId)
                }
            }
        }

    }

//    override fun updateActionBar(comic: Comic?) {
//        super.updateActionBar(comic)
//        menu.findItem(R.id.previous).isEnabled = comic?.let { it.id > 0 } ?: false
//        menu.findItem(R.id.next).isEnabled = comic?.let { it.id < comicViewModel.maxId } ?: false
//    }
}