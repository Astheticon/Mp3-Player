package com.example.musicplayer.utils

fun formatTime(ms: Long): String {
    if (ms <= 0) return "0:00"
    val seconds = (ms / 1000) % 60
    val minutes = (ms / 1000) / 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}
