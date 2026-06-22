package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.repository.PlaybackRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

class ToggleShuffleUseCaseTest {

    private val playbackRepository: PlaybackRepository = mockk()
    private val toggleShuffleUseCase = ToggleShuffleUseCase(playbackRepository)

    @Test
    fun `invoke delegates to playbackRepository toggleShuffle`() {
        every { playbackRepository.toggleShuffle() } just runs

        toggleShuffleUseCase()

        verify(exactly = 1) { playbackRepository.toggleShuffle() }
    }
}
