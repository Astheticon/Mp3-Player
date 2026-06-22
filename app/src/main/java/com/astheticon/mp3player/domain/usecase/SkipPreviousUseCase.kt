package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.repository.PlaybackRepository

class SkipPreviousUseCase(private val playbackRepository: PlaybackRepository) {
    operator fun invoke() {
        playbackRepository.skipPrevious()
    }
}
