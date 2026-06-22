package com.astheticon.mp3player.domain.usecase

import com.astheticon.mp3player.domain.model.Song
import com.astheticon.mp3player.domain.repository.SongRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import app.cash.turbine.test

class GetSongsUseCaseTest {

    private val songRepository: SongRepository = mockk()
    private val getSongsUseCase = GetSongsUseCase(songRepository)

    @Test
    fun `invoke delegates to songRepository getAllSongs`() = runTest {
        val expectedSongs = listOf(
            Song(1, "Title", "Artist", 1000L, "uri")
        )
        every { songRepository.getAllSongs() } returns flowOf(expectedSongs)

        getSongsUseCase().test {
            assertEquals(expectedSongs, awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) { songRepository.getAllSongs() }
    }
}
