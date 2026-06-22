package com.astheticon.mp3player.data.repository

import com.astheticon.mp3player.data.local.MediaStoreDataSource
import com.astheticon.mp3player.data.permission.AudioPermissionChecker
import com.astheticon.mp3player.domain.model.Song
import com.astheticon.mp3player.domain.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MediaStoreSongRepository @Inject constructor(
    private val dataSource: MediaStoreDataSource,
    private val permissionChecker: AudioPermissionChecker
) : SongRepository {

    override fun getAllSongs(): Flow<List<Song>> = flow {
        if (!permissionChecker.hasAudioPermission()) {
            emit(emptyList())
        } else {
            emit(dataSource.getAllSongs())
        }
    }.flowOn(Dispatchers.IO)
}
