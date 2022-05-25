package com.zima.myxkcdviewer.ui

import android.content.Context
import androidx.lifecycle.*
import com.zima.myxkcdviewer.data.logic.repository.ComicRoomRepository
import com.zima.myxkcdviewer.data.models.Comic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicViewModel @Inject constructor(val handle: SavedStateHandle, private val comicRoomRepository: ComicRoomRepository) : ViewModel() {
    // Using LiveData and caching what allFavoriteComics returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    //list of all comics in favorite database
    val allFavoriteComics: LiveData<List<Comic>> = comicRoomRepository.getAllFavoriteComics().asLiveData()

    //if the viewmodel is not initialized it will first determine the maxid using today's comic
    var isInitialized = false

    //id of the currently displayed comic
    var currentId = ID_NOT_INITIALIZED

    //maximum comic id. this is the id of today's comic. this will be determined when the viewmodel is initialized
    var maxId = ID_NOT_INITIALIZED

    //currently active comic
    private val currentComicLiveData = MutableLiveData<Comic>()


    //add a comic to the favorites
    fun insert(context: Context, comic: Comic) = viewModelScope.launch(Dispatchers.IO) {
        val newComic = comic.copyToUniqueUri(context)
        comicRoomRepository.insert(newComic)
    }

    //delete a comic from the favorite list. also move the stored image to the temp image name
    fun delete(context: Context, comic: Comic) = viewModelScope.launch(Dispatchers.IO) {
        val comicFromDAO = comicRoomRepository.getComicFromID(comic.id)

        comicFromDAO?.let {
            comicRoomRepository.deleteComic(it)
            it.moveImageToTempPath(context)
            comic.copyFrom(it)
        }

    }

    //check if a comic exists in the favorites
    fun exists(comic: Comic, block: (Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val exists = comicRoomRepository.exists(comic)
        launch(Dispatchers.Main) { block(exists) }
    }

    fun getCurrentComic() = currentComicLiveData as LiveData<Comic>

    fun setCurrentComic(comic: Comic) {
        currentComicLiveData.value = comic
        currentId = comic.id
    }

    companion object {
        const val ID_NOT_INITIALIZED = -1
    }
}

