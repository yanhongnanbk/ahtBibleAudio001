package com.yan.ahtbibleaudio001.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yan.ahtbibleaudio001.models.movie.Movie
import com.yan.ahtbibleaudio001.remote.NetworkState
import com.yan.ahtbibleaudio001.repository.movie.MoviePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class MovieActivityViewModel(private val movieRepository : MoviePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    // To see if our movie pagelist is empty or not
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}