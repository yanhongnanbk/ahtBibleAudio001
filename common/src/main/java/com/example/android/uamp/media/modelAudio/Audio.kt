package com.example.android.uamp.media.modelAudio

import com.google.gson.annotations.SerializedName

data class Audio(
    @SerializedName("category")
    var category: Int,
    @SerializedName("description")
    var description: String,
    @SerializedName("file_url")
    var fileUrl: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("image_url")
    var imageUrl: String,
    @SerializedName("title")
    var title: String
)