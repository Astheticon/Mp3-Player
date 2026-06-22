package com.astheticon.mp3player.domain.repository

import com.astheticon.mp3player.domain.model.Playlist
import com.astheticon.mp3player.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun createPlaylist(name: String)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun addSongsToPlaylist(playlistId: Long, songIds: List<Long>)
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)
    fun getSongsInPlaylist(playlistId: Long): Flow<List<Song>>
}
