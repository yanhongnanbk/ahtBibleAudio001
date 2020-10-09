package com.yan.ahtbibleaudio001.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY_AUDIO = ""
const val BASE_URL_AUDIO = "http://172.16.1.91:3000/api/v1/"

const val FIRST_PAGE_AUDIO = 1

object AudioClient {
    fun getClient(): APIServiceAudio {
        val requestInterceptor = Interceptor { chain ->
            // Interceptor take only one argument which is a lambda function so parenthesis can be omitted

            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY_AUDIO)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)   //explicitly return a value from whit @ annotation. lambda always returns the value of the last expression implicitly
        }


        // Add Intercepter to the OKHTTP Client
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        // Return Retrofit Builder
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL_AUDIO)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIServiceAudio::class.java)

    }
}