package com.astheticon.mp3player.data.repository

import com.astheticon.mp3player.domain.model.PlaybackState
import com.astheticon.mp3player.domain.model.RepeatMode
import com.astheticon.mp3player.domain.model.Song
import com.astheticon.mp3player.domain.repository.PlaybackRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.Player
import androidx.media3.common.MediaItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerPlaybackRepository @Inject constructor(
    private val exoPlayer: ExoPlayer
) : PlaybackRepository {

    // Internal queue handling – original order preserved for shuffle toggle
    private var songQueue: List<Song> = emptyList()
    private var originalQueue: List<Song> = emptyList()

    // Backing mutable state flow
    private val _playbackState = MutableStateFlow(
        PlaybackState(
            currentSong = null,
            isPlaying = false,
            positionMs = 0L,
            shuffleEnabled = false,
            repeatMode = RepeatMode.OFF
        )
    )
    override val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    // Position tracking coroutine (500 ms interval) – runs on Main dispatcher
    private var positionJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playbackState.update { it.copy(isPlaying = isPlaying) }
                if (isPlaying) startPositionTracking() else stopPositionTracking()
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val uri = mediaItem?.mediaId ?: return
                val song = songQueue.find { it.uri == uri }
                _playbackState.update { it.copy(currentSong = song) }
            }
        })
    }

    private fun startPositionTracking() {
        if (positionJob?.isActive == true) return
        positionJob = scope.launch {
            while (isActive) {
                delay(500)
                _playbackState.update { it.copy(positionMs = exoPlayer.currentPosition) }
            }
        }
    }

    private fun stopPositionTracking() {
        positionJob?.cancel()
        positionJob = null
    }

    // ---------- PlaybackRepository implementation ----------
    override fun play(song: Song) {
        // Initialise queue if empty – treat the provided song as the sole element
        if (songQueue.isEmpty()) {
            songQueue = listOf(song)
            originalQueue = songQueue
            exoPlayer.setMediaItems(songQueue.map { MediaItem.fromUri(it.uri) })
        }
        // Seek to the requested song in the current queue
        val index = songQueue.indexOfFirst { it.id == song.id }
        if (index >= 0) {
            exoPlayer.seekToDefaultPosition(index)
        }
        exoPlayer.playWhenReady = true
        exoPlayer.play()
    }

    override fun togglePlayPause() {
        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
    }

    override fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    override fun skipNext() {
        exoPlayer.seekToNextMediaItem()
    }

    override fun skipPrevious() {
        exoPlayer.seekToPreviousMediaItem()
    }

    override fun toggleShuffle() {
        val newShuffle = !_playbackState.value.shuffleEnabled
        _playbackState.update { it.copy(shuffleEnabled = newShuffle) }
        if (newShuffle) {
            // Preserve original ordering before enabling shuffle
            originalQueue = songQueue
            exoPlayer.shuffleModeEnabled = true
        } else {
            // Restore the original queue order when shuffle is turned off
            songQueue = originalQueue
            exoPlayer.shuffleModeEnabled = false
            exoPlayer.setMediaItems(songQueue.map { MediaItem.fromUri(it.uri) })
        }
    }

    override fun setRepeatMode(mode: RepeatMode) {
        _playbackState.update { it.copy(repeatMode = mode) }
        exoPlayer.repeatMode = when (mode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
    }

    // Called by the Service when it is destroyed
    fun release() {
        stopPositionTracking()
        exoPlayer.release()
    }
}
