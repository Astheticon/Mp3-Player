package com.astheticon.mp3player.domain.model

data class PlaylistSong(
    val playlistId: Long,
    val songId: Long,
    val addedAt: Long
)
