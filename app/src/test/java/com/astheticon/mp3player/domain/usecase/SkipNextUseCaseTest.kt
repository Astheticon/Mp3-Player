package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.repository.PlaybackRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

class SkipNextUseCaseTest {

    private val playbackRepository: PlaybackRepository = mockk()
    private val skipNextUseCase = SkipNextUseCase(playbackRepository)

    @Test
    fun `invoke delegates to playbackRepository skipNext`() {
        every { playbackRepository.skipNext() } just runs

        skipNextUseCase()

        verify(exactly = 1) { playbackRepository.skipNext() }
    }
}
