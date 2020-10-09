package com.example.android.uamp.media.remote

import com.example.android.uamp.media.modelAudio.AudioResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServiceAudio {
    @GET("audio/list/")
    fun getAudio(@Query("page") page: Int): Single<AudioResponse>
}