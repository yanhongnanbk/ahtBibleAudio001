package com.yan.ahtbibleaudio001.repository.audio

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.yan.ahtbibleaudio001.models.audio.Audio
import com.yan.ahtbibleaudio001.remote.APIServiceAudio
import io.reactivex.disposables.CompositeDisposable

class AudioDataSourceFactory (private val apiService : APIServiceAudio, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Audio>() {

    val audiosLiveDataSource =  MutableLiveData<AudioDataSource>()

    override fun create(): DataSource<Int, Audio> {
        val audioDataSource = AudioDataSource(apiService,compositeDisposable)

        audiosLiveDataSource.postValue(audioDataSource)
        return audioDataSource
    }
}