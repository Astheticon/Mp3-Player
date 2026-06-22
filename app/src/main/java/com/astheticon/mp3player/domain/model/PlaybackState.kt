package com.astheticon.mp3player.domain.model

data class PlaybackState(
    val currentSong: Song?,
    val isPlaying: Boolean,
    val positionMs: Long,
    val shuffleEnabled: Boolean,
    val repeatMode: RepeatMode
)
