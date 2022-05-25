package com.zima.myxkcdviewer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zima.myxkcdviewer.R
import com.zima.myxkcdviewer.data.logic.utils.ImageIOHelper
import com.zima.myxkcdviewer.data.models.Comic
import com.zima.myxkcdviewer.databinding.ComicItemViewBinding
import com.zima.myxkcdviewer.interfaces.ComicListClickListener

class ComicListAdapter(private val comicListener: ComicListClickListener) :
    ListAdapter<Comic, ComicListAdapter.ComicViewHolder>(COMICS_COMPARATOR) {

    private lateinit var binding: ComicItemViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        binding = ComicItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, comicListener)

    }

    class ComicViewHolder(private val binding: ComicItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comic: Comic, comicListener: ComicListClickListener) {
            binding.comic = comic
            binding.listener = comicListener
            comic.bitmapPath?.let { ImageIOHelper.setImage(it, binding.ivComic) }

        }

    }

    companion object {
        private val COMICS_COMPARATOR = object : DiffUtil.ItemCallback<Comic>() {
            override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem == newItem
            }
        }
    }

}
