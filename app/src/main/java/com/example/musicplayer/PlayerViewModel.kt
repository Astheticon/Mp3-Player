package com.example.musicplayer

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicplayer.model.Song
import com.example.musicplayer.service.PlaybackService
import com.example.musicplayer.utils.MediaScanner
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class RepeatMode { NONE, REPEAT_ALL, REPEAT_ONE }

class PlayerViewModel(context: Context) : ViewModel() {
    private var player: Player? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var pendingAction: (() -> Unit)? = null
    private var durationPollingJob: Job? = null

    private fun tryUpdateDuration() {
        val raw = player?.duration ?: C.TIME_UNSET
        if (raw != C.TIME_UNSET && raw > 0L) duration = raw
    }

    private fun pollForDuration() {
        durationPollingJob?.cancel()
        durationPollingJob = viewModelScope.launch {
            val maxAttempts = 30 // 30 × 100ms = 3 seconds
            repeat(maxAttempts) {
                val raw = player?.duration ?: C.TIME_UNSET
                if (raw != C.TIME_UNSET && raw > 0L) {
                    duration = raw
                    return@launch // found valid duration — stop polling
                }
                delay(100)
            }
        }
    }

    private fun runWhenReady(action: () -> Unit) {
        if (player != null) {
            action()
        } else {
            pendingAction = action
        }
    }

    init {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            player = controllerFuture?.get()
            player?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        tryUpdateDuration()
                        currentPosition = player?.currentPosition ?: 0L
                    }
                }
                override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                    if (player?.playbackState == Player.STATE_READY) {
                        tryUpdateDuration()
                    }
                }
                override fun onIsPlayingChanged(isPlaying_: Boolean) {
                    isPlaying = isPlaying_
                }
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    currentPosition = 0L
                    duration = 0L
                }
            })
            // Execute any action that was queued before controller was ready
            pendingAction?.invoke()
            pendingAction = null
        }, ContextCompat.getMainExecutor(context))
    }

    var isPlaying by mutableStateOf(false)
    var currentPosition by mutableStateOf(0L)
    var duration by mutableStateOf(0L)
    var fileName by mutableStateOf("")
    var hasFile by mutableStateOf(false)

    var songs by mutableStateOf<List<Song>>(emptyList())
    var currentSongIndex by mutableStateOf(0)
    var isShuffled by mutableStateOf(false)
    var repeatMode by mutableStateOf(RepeatMode.NONE)

    fun loadLibrary(context: Context) {
        songs = MediaScanner.scanForAudio(context)
    }

    fun loadSong(index: Int, autoPlay: Boolean = true) {
        if (index !in songs.indices) return
        currentSongIndex = index
        val song = songs[index]
        fileName = song.title
        hasFile = true
        duration = 0L
        currentPosition = 0L
        runWhenReady {
            player?.setMediaItem(MediaItem.fromUri(song.uri))
            player?.prepare()
            if (autoPlay) {
                player?.play()
            }
            pollForDuration()
        }
    }

    fun playNext() {
        if (songs.isEmpty()) return
        if (repeatMode == RepeatMode.REPEAT_ONE) {
            player?.seekTo(0)
            player?.play()
            return
        }
        var newIndex = currentSongIndex + 1
        if (isShuffled && songs.size > 1) {
            do {
                newIndex = (0 until songs.size).random()
            } while (newIndex == currentSongIndex)
        } else if (newIndex >= songs.size) {
            if (repeatMode == RepeatMode.REPEAT_ALL) {
                newIndex = 0
            } else {
                player?.stop()
                isPlaying = false
                return
            }
        }
        loadSong(newIndex)
    }

    fun playPrevious() {
        if (songs.isEmpty()) return
        if (currentPosition > 3000L) {
            seekTo(0)
        } else {
            var newIndex = currentSongIndex - 1
            if (newIndex < 0) newIndex = songs.lastIndex
            loadSong(newIndex)
        }
    }

    fun toggleShuffle() {
        isShuffled = !isShuffled
    }

    fun toggleRepeat() {
        repeatMode = when (repeatMode) {
            RepeatMode.NONE -> RepeatMode.REPEAT_ALL
            RepeatMode.REPEAT_ALL -> RepeatMode.REPEAT_ONE
            RepeatMode.REPEAT_ONE -> RepeatMode.NONE
        }
    }

    fun loadFile(uri: Uri, context: Context) {
        fileName = uri.lastPathSegment ?: "Unknown File"
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) fileName = cursor.getString(index)
            }
        }
        hasFile = true

        if (songs.none { it.uri == uri }) {
            val newSong = Song(
                id = System.currentTimeMillis(),
                title = fileName,
                artist = Song.UNKNOWN,
                album = Song.UNKNOWN,
                duration = 0L,
                uri = uri,
                albumId = 0L
            )
            songs = songs + newSong
            currentSongIndex = songs.lastIndex
        } else {
            currentSongIndex = songs.indexOfFirst { it.uri == uri }
        }

        runWhenReady {
            player?.setMediaItem(MediaItem.fromUri(uri))
            player?.prepare()
            player?.play()
            pollForDuration()
        }
    }

    fun play() { player?.play(); isPlaying = true }
    fun pause() { player?.pause(); isPlaying = false }
    fun seekTo(position: Long) { player?.seekTo(position); currentPosition = position }

    fun updateProgress() {
        if (player?.playbackState == Player.STATE_READY || player?.playbackState == Player.STATE_BUFFERING) {
            tryUpdateDuration()
            currentPosition = player?.currentPosition ?: 0L
        }
        isPlaying = player?.isPlaying ?: false
        if (player?.playbackState == Player.STATE_ENDED) {
            playNext()
        }
    }

    override fun onCleared() {
        super.onCleared()
        controllerFuture?.let { MediaController.releaseFuture(it) }
    }

    companion object {
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = PlayerViewModel(context) as T
        }
    }
}
