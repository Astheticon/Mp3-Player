package com.astheticon.mp3player.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.exoplayer.ExoPlayer
import com.astheticon.mp3player.data.repository.ExoPlayerPlaybackRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Foreground service that hosts a MediaSession wired to the ExoPlayerPlaybackRepository.
 * It starts as a foreground service when playback begins and stops when playback ends.
 */
@AndroidEntryPoint
class Mp3PlayerService : MediaSessionService() {

    @Inject lateinit var playbackRepository: ExoPlayerPlaybackRepository
    @Inject lateinit var exoPlayer: ExoPlayer

    private lateinit var mediaSession: MediaSession
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "mp3_player_service_channel"

    override fun onCreate() {
        super.onCreate()
        // Create MediaSession linked to the ExoPlayer instance managed by the repository
        mediaSession = MediaSession.Builder(this, exoPlayer).build()
        // Create and start foreground notification
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    override fun onDestroy() {
        mediaSession.release()
        playbackRepository.release()
        super.onDestroy()
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Foreground service for music playback" }
            manager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Mp3 Player")
            .setContentText("Playing music")
            .setSmallIcon(com.astheticon.mp3player.R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()
    }
}
