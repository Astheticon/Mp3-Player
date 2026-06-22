package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.repository.PlaybackRepository

class ToggleShuffleUseCase(private val playbackRepository: PlaybackRepository) {
    operator fun invoke() {
        playbackRepository.toggleShuffle()
    }
}
