package com.yan.ahtbibleaudio001.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yan.ahtbibleaudio001.models.movie.MovieDetail
import com.yan.ahtbibleaudio001.remote.NetworkState
import com.yan.ahtbibleaudio001.repository.movieDetail.MovieDetailRepository
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieRepository : MovieDetailRepository, movieId: Int)  : ViewModel()  {
    //
    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetail> by lazy {
        movieRepository.fetchSingleMovieDetail(compositeDisposable,movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}