package com.astheticon.mp3player.domain.repository

import com.astheticon.mp3player.domain.model.PlaybackState
import com.astheticon.mp3player.domain.model.RepeatMode
import com.astheticon.mp3player.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface PlaybackRepository {
    val playbackState: Flow<PlaybackState>
    
    fun play(song: Song)
    fun togglePlayPause()
    fun seekTo(positionMs: Long)
    fun skipNext()
    fun skipPrevious()
    fun toggleShuffle()
    fun setRepeatMode(mode: RepeatMode)
}
