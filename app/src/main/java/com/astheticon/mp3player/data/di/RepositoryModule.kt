package com.astheticon.mp3player.data.di

import com.astheticon.mp3player.data.local.MediaStoreDataSource
import com.astheticon.mp3player.data.local.MediaStoreDataSourceImpl
import com.astheticon.mp3player.data.repository.MediaStoreSongRepository
import com.astheticon.mp3player.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSongRepository(impl: MediaStoreSongRepository): SongRepository

    @Binds
    @Singleton
    abstract fun bindMediaStoreDataSource(impl: MediaStoreDataSourceImpl): MediaStoreDataSource
}
