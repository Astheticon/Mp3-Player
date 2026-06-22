package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.model.Song
import com.astheticon.mp3player.domain.repository.PlaybackRepository

class PlaySongUseCase(private val playbackRepository: PlaybackRepository) {
    operator fun invoke(song: Song) {
        playbackRepository.play(song)
    }
}
