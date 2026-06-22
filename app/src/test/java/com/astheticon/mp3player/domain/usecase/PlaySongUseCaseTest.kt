package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.model.Song
import com.astheticon.mp3player.domain.repository.PlaybackRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

class PlaySongUseCaseTest {

    private val playbackRepository: PlaybackRepository = mockk()
    private val playSongUseCase = PlaySongUseCase(playbackRepository)

    @Test
    fun `invoke delegates to playbackRepository play`() {
        val song = Song(1, "Title", "Artist", 1000L, "uri")
        every { playbackRepository.play(song) } just runs

        playSongUseCase(song)

        verify(exactly = 1) { playbackRepository.play(song) }
    }
}
