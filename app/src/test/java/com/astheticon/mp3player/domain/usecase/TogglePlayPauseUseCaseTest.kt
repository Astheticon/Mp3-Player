package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.repository.PlaybackRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

class TogglePlayPauseUseCaseTest {

    private val playbackRepository: PlaybackRepository = mockk()
    private val togglePlayPauseUseCase = TogglePlayPauseUseCase(playbackRepository)

    @Test
    fun `invoke delegates to playbackRepository togglePlayPause`() {
        every { playbackRepository.togglePlayPause() } just runs

        togglePlayPauseUseCase()

        verify(exactly = 1) { playbackRepository.togglePlayPause() }
    }
}
