package com.zima.myxkcdviewer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zima.myxkcdviewer.data.models.Comic
import com.zima.myxkcdviewer.databinding.FragmentFavoriteComicListBinding
import com.zima.myxkcdviewer.interfaces.ComicListClickListener
import com.zima.myxkcdviewer.ui.ComicListAdapter
import com.zima.myxkcdviewer.ui.ComicViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class FavoriteComicListFragment : Fragment(), ComicListClickListener {

    private lateinit var binding: FragmentFavoriteComicListBinding

    private var actionBar: ActionBar? = null

    private val comicViewModel: ComicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoriteComicListBinding.inflate(inflater, container, false)

        // Set the adapter
        with(binding.recyclerView) {
//            layoutManager = LinearLayoutManager(context)
            val myAdapter = ComicListAdapter(this@FavoriteComicListFragment)
            adapter = myAdapter
            // Add an observer on the LiveData returned by getAlphabetizedWords.
            // The onChanged() method fires when the observed data changes and the activity is
            // in the foreground.
            comicViewModel.allFavoriteComics.observe(viewLifecycleOwner) { comics ->
                // Update the cached copy of the words in the adapter.
                comics.let { myAdapter.submitList(it) }
            }

            //show a message when list ist empty
            setEmptyView(binding.tvEmpty)
        }


        actionBar = (activity as? AppCompatActivity)?.supportActionBar


        return binding.root
    }

    override fun onSelect(comic: Comic) {
        val action = com.zima.myxkcdviewer.ui.fragments.FavoriteComicListFragmentDirections.actionFavoriteComicListFragmentToFavoriteComicViewFragment(comic)
        findNavController().navigate(action)
    }

    override fun onRemove(comic: Comic) {
        comicViewModel.delete(requireContext(), comic)
    }


}