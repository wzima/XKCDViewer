package com.zima.myxkcdviewer.ui.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Html
import android.util.Log
import com.zima.myxkcdviewer.data.models.Comic
import com.zima.myxkcdviewer.ui.customviews.ToolsView
import java.util.*

class MyTextToSpeechHelper(context: Context, val toolsView: ToolsView) : TextToSpeech.OnInitListener {
    private val tts = TextToSpeech(context, this)
    private var isOperational = false
    var text = ""
        set(value) {
            field = value
            enableReadButton()
        }

    override fun onInit(status: Int) {
        when (status) {
            TextToSpeech.SUCCESS -> {
                // set US English as language for tts
                val result = tts.setLanguage(Locale.US)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeech", "The Language specified is not supported!")
                    isOperational = false
                } else {
                    tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(p0: String?) {
                            toolsView.isReadingLoud(true)
                        }

                        override fun onDone(p0: String?) {
                            toolsView.isReadingLoud(false)
                        }

                        override fun onError(p0: String?) {
                            toolsView.isReadingLoud(false)
                        }
                    })
                    isOperational = true
                }

            }
            else -> {
                Log.e("TextToSpeech", "Failed to Initialize")
                isOperational = false
            }
        }
        enableReadButton()
    }


    private fun enableReadButton() {
        val isEnabled = isOperational && text.isNotEmpty()
        toolsView.enableReadLoud(isEnabled)
    }

    fun stopLoudReadingText() {
        tts.let { textToSpeech ->
            if (textToSpeech.isSpeaking)
                textToSpeech.stop()
            toolsView.isReadingLoud(false)
        }
    }

    fun readText(comic: Comic?) {
        stopLoudReadingText()
        tts.let { textToSpeech ->
            if (textToSpeech.isSpeaking)
                textToSpeech.stop()
            else
                comic?.let {
                    val text = Html.fromHtml(it.transcript).toString()
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
                }
        }
    }

    fun shutdown() = tts.shutdown()
}