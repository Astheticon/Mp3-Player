package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.model.Song
import com.astheticon.mp3player.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow

class GetSongsUseCase(private val songRepository: SongRepository) {
    operator fun invoke(): Flow<List<Song>> {
        return songRepository.getAllSongs()
    }
}
