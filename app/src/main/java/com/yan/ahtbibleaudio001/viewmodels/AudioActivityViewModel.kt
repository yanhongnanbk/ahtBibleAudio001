package com.yan.ahtbibleaudio001.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yan.ahtbibleaudio001.models.audio.Audio
import com.yan.ahtbibleaudio001.remote.NetworkState
import com.yan.ahtbibleaudio001.repository.audio.AudioPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class AudioActivityViewModel(private val audioRepository : AudioPagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  audioPagedList : LiveData<PagedList<Audio>> by lazy {
        audioRepository.fetchLiveAudioPagedList(compositeDisposable)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        audioRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return audioPagedList.value?.isEmpty() ?: true
    }

    // To see if our movie pagelist is empty or not
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}