package com.yan.ahtbibleaudio001.repository.audio

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.yan.ahtbibleaudio001.models.audio.Audio
import com.yan.ahtbibleaudio001.remote.APIServiceAudio
import com.yan.ahtbibleaudio001.remote.FIRST_PAGE_AUDIO
import com.yan.ahtbibleaudio001.remote.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AudioDataSource (private val apiService : APIServiceAudio, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Audio>(){

    private var page = FIRST_PAGE_AUDIO

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Audio>) {

        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getAudio(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.data.audios, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it1 -> Log.e("MovieDataSource", it1) }
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Audio>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            // params.key is the next page number and will be incremented automatically
            apiService.getAudio(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        // if we have more pages to load
                        Log.d("BLAH","${it.data.pagination.lastPage }")
                        if(it.data.pagination.lastPage >= params.key) {
//                            Log.d("BLAH","${params.key}")
                            // we dont have to put prev Page key here like the one in loadInitial
                            callback.onResult(it.data.audios, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it1 -> Log.e("AudioDataSource", it1) }
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Audio>) {

    }
}