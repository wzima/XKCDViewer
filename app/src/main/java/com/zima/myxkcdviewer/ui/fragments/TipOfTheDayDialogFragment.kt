package com.zima.myxkcdviewer.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.zima.myxkcdviewer.R

class TipOfTheDayDialogFragment : DialogFragment() {
    private var textResId = 0
    private var titleResId = 0
    private var preferencesKey: String? = null
    fun setOnDialogClosedListener(onDialogClosedListener: OnDialogClosedListener?): TipOfTheDayDialogFragment {
        this.onDialogClosedListener = onDialogClosedListener
        return this
    }

    private var onDialogClosedListener: OnDialogClosedListener? = null

    fun setTitle(title: String?): TipOfTheDayDialogFragment {
        this.title = title
        val args = arguments
        args!!.putString("title", title)
        arguments = args
        return this
    }

    private var title: String? = null
    override fun onDestroyView() {
        val dialog = dialog
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, theme)
        retainInstance = true
    }

    fun isShow(manager: FragmentManager, tag: String?, context: Context, preferencesKey: String?) {
        if (manager.findFragmentByTag(tag) != null) return

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val showHelp = prefs.getBoolean(preferencesKey, true)
        if (!showHelp) {
            if (onDialogClosedListener != null) onDialogClosedListener!!.dialogClosed()
        } else try {
            show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        textResId = requireArguments().getInt("textResId")
        title = requireArguments().getString("title")
        titleResId = requireArguments().getInt("titleResId")
        preferencesKey = requireArguments().getString("preferencesKey")

        val builder = AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert)
        val view = requireActivity().layoutInflater.inflate(R.layout.tip_of_the_day, null)
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val tvTitle = view.findViewById<View>(R.id.textViewTitle) as TextView

        if (title != null) tvTitle.text = title else tvTitle.setText(titleResId)
        val tvHelp = view.findViewById<View>(R.id.textView) as TextView
        tvHelp.text = Html.fromHtml(requireContext().getString(textResId))
        val checkBox = view.findViewById<View>(R.id.checkBox) as CheckBox
        val button = view.findViewById<View>(R.id.buttonClose) as Button

        button.setOnClickListener {
            val editor = prefs.edit()
            editor.putBoolean(preferencesKey, !checkBox.isChecked)
            editor.apply()
            dismiss()
            if (onDialogClosedListener != null) onDialogClosedListener!!.dialogClosed()
        }

        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnCancelListener {
            val editor = prefs.edit()
            editor.putBoolean(preferencesKey, !checkBox.isChecked)
            editor.apply()
            dismiss()
            if (onDialogClosedListener != null) onDialogClosedListener!!.dialogClosed()
        }
        return dialog
    }

    interface OnDialogClosedListener {
        fun dialogClosed()
    }

    companion object {
        const val INTRO_TIP = "INTRO_TIP"
        const val IMAGE_VIEW_TIP = "IMAGE_VIEW_TIP2"
        fun newInstance(titleResId: Int, textResId: Int, preferencesKey: String?): TipOfTheDayDialogFragment {
            val frag = TipOfTheDayDialogFragment()
            val args = Bundle()
            args.putInt("textResId", textResId)
            args.putInt("titleResId", titleResId)
            args.putString("preferencesKey", preferencesKey)
            frag.arguments = args
            return frag
        }
    }
}