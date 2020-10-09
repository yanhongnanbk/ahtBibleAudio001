package com.yan.ahtbibleaudio001.repository.movieDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yan.ahtbibleaudio001.models.movie.MovieDetail
import com.yan.ahtbibleaudio001.remote.APIService
import com.yan.ahtbibleaudio001.remote.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailDataSource(private val apiService: APIService, private val compositeDisposable: CompositeDisposable) {

    private val _networkState: MutableLiveData<NetworkState> = MutableLiveData()
    val networkState:LiveData<NetworkState>
    get() = _networkState
    // The get method will be called everytime we access the networkstate, so we dont need to implement setter and getter

    private val _movieDetailsResponse =  MutableLiveData<MovieDetail>()
    val downloadedMovieResponse: LiveData<MovieDetail>
        get() = _movieDetailsResponse

    fun fetchMovieDetail(movieId:Int){
        _networkState.postValue(NetworkState.LOADING)

        try {
            //this thread should be disposable
            compositeDisposable.add(
                apiService.getMovieDetail(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            // success
                            _movieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },{
                            // failure
                            _networkState.postValue(NetworkState.ERROR)
                            it.message?.let { it1 -> Log.e("MovieDetailsDataSource", it1) }
                        }
                    )

            )
        }catch (e: Exception){
            e.message?.let { Log.e("MovieDetailsDataSource", it) }
        }

    }

}