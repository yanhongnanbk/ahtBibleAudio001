package com.example.android.uamp.media.modelAudio


import com.google.gson.annotations.SerializedName

data class AudioResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("success")
    val success: Boolean
)