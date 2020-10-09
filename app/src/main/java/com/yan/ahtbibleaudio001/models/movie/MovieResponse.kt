package com.yan.ahtbibleaudio001.models.movie


import com.google.gson.annotations.SerializedName
import com.yan.ahtbibleaudio001.models.movie.Movie

data class MovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movieList: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)