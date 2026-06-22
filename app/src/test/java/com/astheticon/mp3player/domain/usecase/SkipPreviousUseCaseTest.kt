package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.repository.PlaybackRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

class SkipPreviousUseCaseTest {

    private val playbackRepository: PlaybackRepository = mockk()
    private val skipPreviousUseCase = SkipPreviousUseCase(playbackRepository)

    @Test
    fun `invoke delegates to playbackRepository skipPrevious`() {
        every { playbackRepository.skipPrevious() } just runs

        skipPreviousUseCase()

        verify(exactly = 1) { playbackRepository.skipPrevious() }
    }
}
