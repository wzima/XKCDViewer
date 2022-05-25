package com.zima.myxkcdviewer.interfaces

import com.zima.myxkcdviewer.data.models.Comic

interface ComicListClickListener {
    fun onSelect(comic: Comic)
    fun onRemove(comic: Comic)
}