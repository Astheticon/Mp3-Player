package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.repository.PlaybackRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

class SeekUseCaseTest {

    private val playbackRepository: PlaybackRepository = mockk()
    private val seekUseCase = SeekUseCase(playbackRepository)

    @Test
    fun `invoke delegates to playbackRepository seekTo`() {
        val positionMs = 5000L
        every { playbackRepository.seekTo(positionMs) } just runs

        seekUseCase(positionMs)

        verify(exactly = 1) { playbackRepository.seekTo(positionMs) }
    }
}
