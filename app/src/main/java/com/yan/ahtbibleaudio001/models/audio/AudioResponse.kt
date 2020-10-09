package com.yan.ahtbibleaudio001.models.audio


import com.google.gson.annotations.SerializedName

data class AudioResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("success")
    val success: Boolean
)