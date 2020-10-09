package com.yan.ahtbibleaudio001.views.activities
import com.google.android.exoplayer2.util.Util
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.yan.ahtbibleaudio001.R
import com.yan.ahtbibleaudio001.zutil000.Samples
import kotlinx.android.synthetic.main.content_play_audio_activity.*


class PlayAudioActivity : AppCompatActivity() {
    private var playWhenReady = true
    private var playbackPosition: Long = 0
    var musikUrl: String? = null
    lateinit var musikUrl1: Uri
    private var mSimpleExoPlayer: SimpleExoPlayer? = null
    private var mPlayerControlView: PlayerControlView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_audio)
//        val intent = Intent(this, AudioPlayerService::class.java)
//        Util.startForegroundService(this,intent)
        musikUrl= intent.getStringExtra("MOVIE_URL")
        musikUrl1 = Samples.SAMPLES[1].uri
        musik_artist.text = musikUrl
        initExoPlayer()
        Toast.makeText(this,"$musikUrl",Toast.LENGTH_SHORT).show()
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        /*First, we create a DefaultDataSourceFactory, specifying our context and
         the user-agent string which will be used when making the HTTP request for the media file.*/
        val dataSourceFactory =
            DefaultDataSourceFactory(baseContext, "Music")
        /*Next, we pass our newly created dataSourceFactory to ProgressiveMediaSource.Factory.
         This is, as the name suggests, a factory for creating progressive media data sources.
         By default the ProgressiveMediaSource.Factory uses a DefaultExtractorsFactory.
         */
        /*Finally, we call createMediaSource, supplying our uri, to obtain a MediaSource object.*/
        return ProgressiveMediaSource.Factory(
            dataSourceFactory
        ).createMediaSource(uri)
    }

    /******************************************Init exo */
    private fun initExoPlayer() {
        mPlayerControlView = findViewById(R.id.player_view_music)
        //Initialize Simple Exo player
        mSimpleExoPlayer = SimpleExoPlayer.Builder(baseContext).build()
        //Initialize data source factory
        val mediaSource =
            buildMediaSource(musikUrl1)
        //Set player
        mPlayerControlView!!.setPlayer(mSimpleExoPlayer)
        //Keep Screen on
        mPlayerControlView!!.keepScreenOn = true
        mSimpleExoPlayer!!.setPlayWhenReady(playWhenReady)
        if (playbackPosition > 0) {
            mSimpleExoPlayer!!.seekTo(playbackPosition)
        } else {
            mSimpleExoPlayer!!.seekTo(0)
        }
        mSimpleExoPlayer!!.prepare(mediaSource!!, false, false)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            if (mSimpleExoPlayer == null) {
                initExoPlayer()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || mSimpleExoPlayer == null) {
            initExoPlayer()
        }
        Toast.makeText(this, "on resume$playbackPosition", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
        playWhenReady = true
        playbackPosition = Math.max(0, mSimpleExoPlayer!!.currentPosition)
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
        playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (mSimpleExoPlayer != null) {
            playWhenReady = mSimpleExoPlayer!!.playWhenReady
            playbackPosition = mSimpleExoPlayer!!.currentPosition
            mSimpleExoPlayer!!.release()
            mSimpleExoPlayer = null
        }
    }



}
