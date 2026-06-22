package com.astheticon.mp3player.domain.repository

import com.astheticon.mp3player.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun getAllSongs(): Flow<List<Song>>
}
