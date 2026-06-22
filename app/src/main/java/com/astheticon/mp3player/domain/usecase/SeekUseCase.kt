package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.repository.PlaybackRepository

class SeekUseCase(private val playbackRepository: PlaybackRepository) {
    operator fun invoke(positionMs: Long) {
        playbackRepository.seekTo(positionMs)
    }
}
