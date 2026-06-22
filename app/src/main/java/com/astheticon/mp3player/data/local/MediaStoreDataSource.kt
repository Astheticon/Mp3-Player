package com.astheticon.mp3player.data.local

import com.astheticon.mp3player.domain.model.Song

interface MediaStoreDataSource {
    suspend fun getAllSongs(): List<Song>
}
