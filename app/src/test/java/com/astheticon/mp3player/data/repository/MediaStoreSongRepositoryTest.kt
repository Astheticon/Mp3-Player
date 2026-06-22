package com.astheticon.mp3player.data.repository

import com.astheticon.mp3player.data.local.MediaStoreDataSource
import com.astheticon.mp3player.data.permission.AudioPermissionChecker
import com.astheticon.mp3player.domain.model.Song
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaStoreSongRepositoryTest {

    private val dataSource: MediaStoreDataSource = mockk()
    private val permissionChecker: AudioPermissionChecker = mockk()
    private val repository = MediaStoreSongRepository(dataSource, permissionChecker)

    private val sampleSongs = listOf(
        Song(1L, "Song A", "Artist X", 180000L, "content://media/external/audio/media/1"),
        Song(2L, "Song B", "Artist Y", 240000L, "content://media/external/audio/media/2")
    )

    @Test
    fun `getAllSongs emits song list when permission is granted`() = runTest {
        every { permissionChecker.hasAudioPermission() } returns true
        coEvery { dataSource.getAllSongs() } returns sampleSongs

        repository.getAllSongs().test {
            assertEquals(sampleSongs, awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { dataSource.getAllSongs() }
    }

    @Test
    fun `getAllSongs emits empty list and never calls dataSource when permission is denied`() = runTest {
        every { permissionChecker.hasAudioPermission() } returns false

        repository.getAllSongs().test {
            val result = awaitItem()
            assertTrue(result.isEmpty())
            awaitComplete()
        }

        coVerify(exactly = 0) { dataSource.getAllSongs() }
    }
}
