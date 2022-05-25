package com.zima.myxkcdviewer.interfaces

import com.zima.myxkcdviewer.R

enum class SuccessStatus(val descriptionResId: Int) {
    Success(R.string.Success), UnknownError(R.string.UnknownError), NoConnection(R.string.NoConnection), UnknownComicId(R.string.UnknownComicId), NoImage(R.string.NoImage), JsonReadError(R.string.JsonReadError)


}
