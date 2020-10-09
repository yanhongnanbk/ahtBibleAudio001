package com.yan.ahtbibleaudio001.repository.movieDetail

import androidx.lifecycle.LiveData
import com.yan.ahtbibleaudio001.models.movie.MovieDetail
import com.yan.ahtbibleaudio001.remote.APIService
import com.yan.ahtbibleaudio001.remote.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository(private val apiService : APIService) {

    lateinit var movieDetailDataSource: MovieDetailDataSource

    fun fetchSingleMovieDetail (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetail> {

        movieDetailDataSource = MovieDetailDataSource(apiService,compositeDisposable)
        movieDetailDataSource.fetchMovieDetail(movieId)

        return movieDetailDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailDataSource.networkState
    }
}