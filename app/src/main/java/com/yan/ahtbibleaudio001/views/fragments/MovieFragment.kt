package com.yan.ahtbibleaudio001.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.GridLayoutManager

import com.yan.ahtbibleaudio001.R
import com.yan.ahtbibleaudio001.remote.APIService
import com.yan.ahtbibleaudio001.remote.MovieClient
import com.yan.ahtbibleaudio001.remote.NetworkState
import com.yan.ahtbibleaudio001.repository.movie.MoviePagedListRepository
import com.yan.ahtbibleaudio001.viewmodels.MovieActivityViewModel
import com.yan.ahtbibleaudio001.views.adapters.PopularMoviePagedListAdapter

import kotlinx.android.synthetic.main.fragment_movie.view.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {


    private lateinit var viewModel: MovieActivityViewModel
    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_movie, container, false)
        val apiService: APIService = MovieClient.getClient()

        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter(requireActivity())

        val gridLayoutManager = GridLayoutManager(requireActivity(), 1)

        // we want the progressbar to show the full width
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else return 1                                             // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        }

        rootView.rv_movie_list.layoutManager = gridLayoutManager
        rootView.rv_movie_list.setHasFixedSize(true)
        rootView.rv_movie_list.adapter = movieAdapter

        viewModel.moviePagedList.observe(requireActivity(), Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(requireActivity(), Observer {
            rootView.progress_bar_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            rootView.txt_error_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
        return rootView
    }

    private fun getViewModel(): MovieActivityViewModel {
        return ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieActivityViewModel(movieRepository) as T
            }
        })[MovieActivityViewModel::class.java]
    }
}
