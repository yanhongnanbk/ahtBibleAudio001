package com.yan.ahtbibleaudio001.models.audio


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("audios")
    val audios: List<Audio>,
    @SerializedName("pagination")
    val pagination: Pagination
)