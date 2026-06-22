package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.model.RepeatMode
import com.astheticon.mp3player.domain.repository.PlaybackRepository

class SetRepeatModeUseCase(private val playbackRepository: PlaybackRepository) {
    operator fun invoke(mode: RepeatMode) {
        playbackRepository.setRepeatMode(mode)
    }
}
