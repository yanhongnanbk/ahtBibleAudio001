package com.yan.ahtbibleaudio001.repository.movie

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.yan.ahtbibleaudio001.models.movie.Movie
import com.yan.ahtbibleaudio001.remote.APIService
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService : APIService, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}