package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.model.RepeatMode
import com.astheticon.mp3player.domain.repository.PlaybackRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

class SetRepeatModeUseCaseTest {

    private val playbackRepository: PlaybackRepository = mockk()
    private val setRepeatModeUseCase = SetRepeatModeUseCase(playbackRepository)

    @Test
    fun `invoke delegates to playbackRepository setRepeatMode`() {
        val mode = RepeatMode.ONE
        every { playbackRepository.setRepeatMode(mode) } just runs

        setRepeatModeUseCase(mode)

        verify(exactly = 1) { playbackRepository.setRepeatMode(mode) }
    }
}
