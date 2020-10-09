package com.example.android.uamp.media.modelAudio

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("currentPage")
    val currentPage: String,
    @SerializedName("from")
    val from: Int,
    @SerializedName("lastPage")
    val lastPage: Int,
    @SerializedName("perPage")
    val perPage: Int,
    @SerializedName("to")
    val to: Int,
    @SerializedName("total")
    val total: Int
)