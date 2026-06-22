package com.astheticon.mp3player.domain.repository

import com.astheticon.mp3player.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavorites(): Flow<List<Song>>
    suspend fun addFavorite(songId: Long)
    suspend fun removeFavorite(songId: Long)
    fun isFavorite(songId: Long): Flow<Boolean>
}
