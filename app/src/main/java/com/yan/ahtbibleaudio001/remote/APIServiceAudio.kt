package com.yan.ahtbibleaudio001.remote

import com.yan.ahtbibleaudio001.models.audio.AudioResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServiceAudio {
    @GET("audio/list/")
    fun getAudio(@Query("page") page: Int): Single<AudioResponse>
}