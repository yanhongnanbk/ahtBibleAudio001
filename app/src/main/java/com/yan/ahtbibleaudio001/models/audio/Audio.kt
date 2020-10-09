package com.yan.ahtbibleaudio001.models.audio


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Audio(
    @SerializedName("category")
    val category: Int,
    @SerializedName("description")
    val description: String?,
    @SerializedName("file_url")
    val fileUrl: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("title")
    val title: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(category)
        parcel.writeString(description)
        parcel.writeString(fileUrl)
        parcel.writeInt(id)
        parcel.writeString(imageUrl)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Audio> {
        override fun createFromParcel(parcel: Parcel): Audio {
            return Audio(parcel)
        }

        override fun newArray(size: Int): Array<Audio?> {
            return arrayOfNulls(size)
        }
    }
}