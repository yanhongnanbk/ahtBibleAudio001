package com.yan.ahtbibleaudio001.remote

import com.yan.ahtbibleaudio001.models.movie.MovieDetail
import com.yan.ahtbibleaudio001.models.movie.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
//https://api.themoviedb.org/3/movie/popular?api_key=6e82726f06caa4456cd288a7015ece08&page=1
//https://api.themoviedb.org/3/movie/550?api_key=6e82726f06caa4456cd288a7015ece08
//https://api.themoviedb.org/3/


interface APIService {

    // we dont need a series of data. That's why we use Single here as return type
    // The annotation Path is used to declare id as parameter which was passed in the @GET

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") id: Int): Single<MovieDetail>

    // page is the query in the link &page=1
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>
}