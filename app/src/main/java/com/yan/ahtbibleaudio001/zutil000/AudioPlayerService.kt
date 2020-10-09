package com.yan.ahtbibleaudio001.zutil000

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.media.app.NotificationCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.yan.ahtbibleaudio001.R
import com.yan.ahtbibleaudio001.views.activities.PlayAudioActivity
import com.yan.ahtbibleaudio001.zutil000.C.PLAYBACK_CHANNEL_ID
import com.yan.ahtbibleaudio001.zutil000.C.PLAYBACK_NOTIFICATION_ID
import com.yan.ahtbibleaudio001.zutil000.Samples.SAMPLES
import com.yan.ahtbibleaudio001.zutil000.Samples.getBitmap

class AudioPlayerService : Service() {
    lateinit var player: SimpleExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager
    override fun onCreate() {
        super.onCreate()
        val context: Context = this
        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(
            trackSelector.buildUponParameters().setMaxVideoSizeSd()
        )
        player = SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
        val defaultDatasourceFactory: DefaultDataSourceFactory = DefaultDataSourceFactory(
            context, Util.getUserAgent(context, "AudioDemo")
        )

        val concatenatingMediaSource = ConcatenatingMediaSource()
        for (sample in SAMPLES) {
            val mediaItem = MediaItem.Builder()
                .setUri(sample.uri)
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()
            val mediaSource: MediaSource =
                ProgressiveMediaSource.Factory(defaultDatasourceFactory).createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        player.setMediaSource(concatenatingMediaSource)
        player.prepare()
        player.playWhenReady = true
        // Player Notification Channel
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            PLAYBACK_CHANNEL_ID,
            R.string.playback_channel_name,
            0,
            PLAYBACK_NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): CharSequence {
                    return SAMPLES[player.currentWindowIndex].title
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    val intent = Intent(context, PlayAudioActivity::class.java)
                    return PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                override fun getCurrentContentText(player: Player): CharSequence? {
                    return SAMPLES[player.currentWindowIndex].description
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return getBitmap(context, SAMPLES[player.currentWindowIndex].bitmapResource)
                }
            },
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    stopSelf()

                }

                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    startForeground(notificationId, notification)
                }
            }
        )
        // omit skip previous and next actions
//        playerNotificationManager.setUseNavigationActions(false);
// omit fast forward action by setting the increment to zer
        val mediaStyle = NotificationCompat.MediaStyle()
        mediaStyle.setShowCancelButton(true)

        playerNotificationManager.setUseNavigationActionsInCompactView(true)
        playerNotificationManager.setPriority(PRIORITY_MAX)
        playerNotificationManager.setUseChronometer(true)
        playerNotificationManager.setUseStopAction(false)
//        playerNotificationManager.setMediaSessionToken(token)
        playerNotificationManager.setPlayer(player)

    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        playerNotificationManager.setPlayer(null)
        stopForeground(false)

    }

    override fun onBind(intent: Intent?): IBinder? {
//        TODO("Not yet implemented")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
}