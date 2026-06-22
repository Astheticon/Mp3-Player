package com.astheticon.mp3player.domain.model

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val durationMs: Long,
    val uri: String
)
