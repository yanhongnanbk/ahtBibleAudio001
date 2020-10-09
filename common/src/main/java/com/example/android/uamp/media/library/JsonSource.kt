/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.uamp.media.library

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat.STATUS_NOT_DOWNLOADED
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.example.android.uamp.media.extensions.album
import com.example.android.uamp.media.extensions.albumArtUri
import com.example.android.uamp.media.extensions.artist
import com.example.android.uamp.media.extensions.displayDescription
import com.example.android.uamp.media.extensions.displayIconUri
import com.example.android.uamp.media.extensions.displaySubtitle
import com.example.android.uamp.media.extensions.displayTitle
import com.example.android.uamp.media.extensions.downloadStatus
import com.example.android.uamp.media.extensions.duration
import com.example.android.uamp.media.extensions.flag
import com.example.android.uamp.media.extensions.genre
import com.example.android.uamp.media.extensions.id
import com.example.android.uamp.media.extensions.mediaUri
import com.example.android.uamp.media.extensions.title
import com.example.android.uamp.media.extensions.trackCount
import com.example.android.uamp.media.extensions.trackNumber
import com.example.android.uamp.media.modelAudio.Audio
import com.example.android.uamp.media.modelAudio.Data
import com.example.android.uamp.media.modelMovie.Movie
import com.example.android.uamp.media.modelMovie.MovieResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * Source of [MediaMetadataCompat] objects created from a basic JSON stream.
 * The definition of the JSON is specified in the docs of [JsonMusic] in this file,
 * which is the object representation of it.
 */
class JsonSource(private val source: Uri) : AbstractMusicSource() {

    val musikList = arrayOf(
        "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/01_-_Intro_-_The_Way_Of_Waking_Up_feat_Alan_Watts.mp3",
        "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/02_-_Geisha.mp3",
        "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/03_-_Voyage_I_-_Waterfall.mp3",
        "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/04_-_The_Music_In_You.mp3",
        "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/06_-_No_Pain_No_Gain.mp3"
    )

    val imageList = arrayOf(
        "https://i.postimg.cc/PxymVMcW/aa.jpg",
        "https://i.postimg.cc/QCnchdjV/ac.jpg",
        "https://i.postimg.cc/Y0SQ5WKJ/ab.jpg",
        "https://i.postimg.cc/fTJx5mvd/ad.jpg",
        "https://i.postimg.cc/Pf6Wm21h/af.jpg"
    )

    val trackList = arrayOf(
        1,
        2,
        3,
        4,
        5
    )

    private var catalog: List<MediaMetadataCompat> = emptyList()

    init {
        state = STATE_INITIALIZING
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = catalog.iterator()

    override suspend fun load() {
        updateCatalog(source)?.let { updatedCatalog ->
            catalog = updatedCatalog
            state = STATE_INITIALIZED
        } ?: run {
            catalog = emptyList()
            state = STATE_ERROR
        }
    }

    /**
     * Function to connect to a remote URI and download/process the JSON file that corresponds to
     * [MediaMetadataCompat] objects.
     */
    private suspend fun updateCatalog(catalogUri: Uri): List<MediaMetadataCompat>? {
        return withContext(Dispatchers.IO) {
            val musicCat = try {
//                downloadJson(catalogUri)
                downloadJsonRetrofit(catalogUri)
            } catch (ioException: IOException) {
                return@withContext null
            }

            // Get the base URI to fix up relative references later.
//            val baseUri = catalogUri.toString().removeSuffix(catalogUri.lastPathSegment ?: "")

            val mediaMetadataCompats = musicCat.data?.audios?.map { song ->
                // The JSON may have paths that are relative to the source of the JSON
                // itself. We need to fix them up here to turn them into absolute paths.
                catalogUri.scheme?.let { scheme ->
                    song.fileUrl = musikList.random()
//                    song.fileUrl = musikList.random()
                    song.imageUrl = imageList.random()
//                    song.imageUrl = imageList.random()
                }

                MediaMetadataCompat.Builder()
                    .from(song)
                    .apply {
                        displayIconUri = song.imageUrl // Used by ExoPlayer and Notification
                        albumArtUri = song.imageUrl
                    }
                    .build()
            }?.toList()
            // Add description keys to be used by the ExoPlayer MediaSession extension when
            // announcing metadata changes.
            if (mediaMetadataCompats != null) {
                mediaMetadataCompats.forEach { it.description.extras?.putAll(it.bundle) }
            }
            mediaMetadataCompats
        }
    }

//    private suspend fun updateCatalog(catalogUri: Uri): List<MediaMetadataCompat>? {
//        return withContext(Dispatchers.IO) {
//            val musicCat = try {
////                downloadJson(catalogUri)
//                downloadJsonRetrofitMovie(catalogUri)
//            } catch (ioException: IOException) {
//                return@withContext null
//            }
//
//            // Get the base URI to fix up relative references later.
////            val baseUri = catalogUri.toString().removeSuffix(catalogUri.lastPathSegment ?: "")
//
//            val mediaMetadataCompats = musicCat.movieList.map { song ->
//                // The JSON may have paths that are relative to the source of the JSON
//                // itself. We need to fix them up here to turn them into absolute paths.
//                catalogUri.scheme?.let { scheme ->
//        //                    if (!song.fileUrl.startsWith(scheme)) {
//        //                         song.fileUrl = musikList.random()
//        //                    }
//                    song.releaseDate = musikList.random()
//        //                    if (!song.imageUrl.startsWith(scheme)) {
//        //                        song.imageUrl = imageList.random()
//        //                    }
//                    song.posterPath = imageList.random()
//                }
//
//                MediaMetadataCompat.Builder()
//                    .from(song)
//                    .apply {
//                        displayIconUri = song.posterPath // Used by ExoPlayer and Notification
//                        albumArtUri = song.posterPath
//                    }
//                    .build()
//            }?.toList()
//            // Add description keys to be used by the ExoPlayer MediaSession extension when
//            // announcing metadata changes.
//            if (mediaMetadataCompats != null) {
//                mediaMetadataCompats.forEach { it.description.extras?.putAll(it.bundle) }
//            }
//            mediaMetadataCompats
//        }
}


/**
 * Attempts to download a catalog from a given Uri.
 *
 * @param catalogUri URI to attempt to download the catalog form.
 * @return The catalog downloaded, or an empty catalog if an error occurred.
 */
@Throws(IOException::class)
private fun downloadJson(catalogUri: Uri): JsonCatalog {
    val catalogConn = URL(catalogUri.toString())
    val reader = BufferedReader(InputStreamReader(catalogConn.openStream()))
    return Gson().fromJson(reader, JsonCatalog::class.java)
}

@Throws(IOException::class)
private fun downloadJsonRetrofit(catalogUri: Uri): RetrofitAudioCatalog {
    val catalogConn = URL(catalogUri.toString())
    val reader = BufferedReader(InputStreamReader(catalogConn.openStream()))
    val result = Gson().fromJson(reader, RetrofitAudioCatalog::class.java)
    Log.d("GSON", result.data?.audios?.get(0)?.title)
    return result
}

@Throws(IOException::class)
private fun downloadJsonRetrofitMovie(catalogUri: Uri): RetrofitMovieCatalog {
    val catalogConn = URL(catalogUri.toString())
    val reader = BufferedReader(InputStreamReader(catalogConn.openStream()))
    val result = Gson().fromJson(reader, RetrofitMovieCatalog::class.java)
    Log.d("GSON1", result.movieList.toString())
    Log.d("GSONLink", catalogConn.toString())
    return result
}


/**
 * Wrapper object for our JSON in order to be processed easily by GSON.
 */
class JsonCatalog {
    var music: List<JsonMusic> = ArrayList()
}

class RetrofitAudioCatalog {
    var data: Data? = null
}

class RetrofitMovieCatalog {
    var movieList: List<Movie> = ArrayList()
//    var movieResponse: MovieResponse? = null
}


/**
 * Extension method for [MediaMetadataCompat.Builder] to set the fields from
 * our JSON constructed object (to make the code a bit easier to see).
 */
fun MediaMetadataCompat.Builder.from(jsonMusic: JsonMusic): MediaMetadataCompat.Builder {
    // The duration from the JSON is given in seconds, but the rest of the code works in
    // milliseconds. Here's where we convert to the proper units.
//    val durationMs = TimeUnit.SECONDS.toMillis(jsonMusic.duration)

    id = jsonMusic.id
    title = jsonMusic.title
    artist = jsonMusic.artist
    album = jsonMusic.album
    duration = TimeUnit.SECONDS.toMillis(jsonMusic.duration)
    genre = jsonMusic.genre
    mediaUri = jsonMusic.source
    albumArtUri = jsonMusic.image
    trackNumber = jsonMusic.trackNumber
    trackCount = jsonMusic.totalTrackCount
    flag = MediaItem.FLAG_PLAYABLE

    // To make things easier for *displaying* these, set the display properties as well.
    displayTitle = jsonMusic.title
    displaySubtitle = jsonMusic.artist
    displayDescription = jsonMusic.album
    displayIconUri = jsonMusic.image

    // Add downloadStatus to force the creation of an "extras" bundle in the resulting
    // MediaMetadataCompat object. This is needed to send accurate metadata to the
    // media session during updates.
    downloadStatus = STATUS_NOT_DOWNLOADED

    // Allow it to be used in the typical builder style.
    return this
}

fun MediaMetadataCompat.Builder.from(jsonMusic: Audio): MediaMetadataCompat.Builder {
    // The duration from the JSON is given in seconds, but the rest of the code works in
    // milliseconds. Here's where we convert to the proper units.
//    val durationMs = TimeUnit.SECONDS.toMillis(jsonMusic.duration)

    id = jsonMusic.id.toString()
    title = jsonMusic.title
    flag = MediaItem.FLAG_PLAYABLE
    album = jsonMusic.category.toString()
    mediaUri = jsonMusic.fileUrl
    artist = jsonMusic.title
    // To make things easier for *displaying* these, set the display properties as well.
    displayTitle = jsonMusic.title
    displayIconUri = jsonMusic.imageUrl
    displayDescription = jsonMusic.description
    displaySubtitle = jsonMusic.title
    trackNumber = Random.nextLong(1,10)
    // Add downloadStatus to force the creation of an "extras" bundle in the resulting
    // MediaMetadataCompat object. This is needed to send accurate metadata to the
    // media session during updates.
    downloadStatus = STATUS_NOT_DOWNLOADED

    // Allow it to be used in the typical builder style.
    return this
}

fun MediaMetadataCompat.Builder.from(jsonMusic: Movie): MediaMetadataCompat.Builder {
    // The duration from the JSON is given in seconds, but the rest of the code works in
    // milliseconds. Here's where we convert to the proper units.
//    val durationMs = TimeUnit.SECONDS.toMillis(jsonMusic.duration)

    id = jsonMusic.id.toString()
    title = jsonMusic.title
    flag = MediaItem.FLAG_PLAYABLE
    artist = jsonMusic.title
    album = jsonMusic.title
    // To make things easier for *displaying* these, set the display properties as well.
    displayTitle = jsonMusic.title
    displayIconUri = jsonMusic.posterPath
    displayDescription = jsonMusic.releaseDate

    displaySubtitle = jsonMusic.title

    // Add downloadStatus to force the creation of an "extras" bundle in the resulting
    // MediaMetadataCompat object. This is needed to send accurate metadata to the
    // media session during updates.
    downloadStatus = STATUS_NOT_DOWNLOADED

    // Allow it to be used in the typical builder style.
    return this
}


/**
 * An individual piece of music included in our JSON catalog.
 * The format from the server is as specified:
 * ```
 *     { "music" : [
 *     { "title" : // Title of the piece of music
 *     "album" : // Album title of the piece of music
 *     "artist" : // Artist of the piece of music
 *     "genre" : // Primary genre of the music
 *     "source" : // Path to the music, which may be relative
 *     "image" : // Path to the art for the music, which may be relative
 *     "trackNumber" : // Track number
 *     "totalTrackCount" : // Track count
 *     "duration" : // Duration of the music in seconds
 *     "site" : // Source of the music, if applicable
 *     }
 *     ]}
 * ```
 *
 * `source` and `image` can be provided in either relative or
 * absolute paths. For example:
 * ``
 *     "source" : "https://www.example.com/music/ode_to_joy.mp3",
 *     "image" : "ode_to_joy.jpg"
 * ``
 *
 * The `source` specifies the full URI to download the piece of music from, but
 * `image` will be fetched relative to the path of the JSON file itself. This means
 * that if the JSON was at "https://www.example.com/json/music.json" then the image would be found
 * at "https://www.example.com/json/ode_to_joy.jpg".
 */
@Suppress("unused")
class JsonMusic {
    var id: String = ""
    var title: String = ""
    var album: String = ""
    var artist: String = ""
    var genre: String = ""
    var source: String = ""
    var image: String = ""
    var trackNumber: Long = 0
    var totalTrackCount: Long = 0
    var duration: Long = -1
    var site: String = ""
}
