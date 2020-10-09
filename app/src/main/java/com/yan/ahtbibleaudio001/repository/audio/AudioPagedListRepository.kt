package com.yan.ahtbibleaudio001.repository.audio

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yan.ahtbibleaudio001.models.audio.Audio
import com.yan.ahtbibleaudio001.remote.APIServiceAudio
import com.yan.ahtbibleaudio001.remote.NetworkState
import com.yan.ahtbibleaudio001.remote.POST_PER_PAGE
import io.reactivex.disposables.CompositeDisposable

class AudioPagedListRepository (private val apiService: APIServiceAudio) {

    lateinit var audioPagedList: LiveData<PagedList<Audio>>
    lateinit var audiosDataSourceFactory: AudioDataSourceFactory

    fun fetchLiveAudioPagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Audio>> {
        audiosDataSourceFactory = AudioDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        audioPagedList = LivePagedListBuilder(audiosDataSourceFactory, config).build()

        return audioPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<AudioDataSource, NetworkState>(
            audiosDataSourceFactory.audiosLiveDataSource, AudioDataSource::networkState)
    }
}