package com.astheticon.mp3player.data.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.astheticon.mp3player.data.repository.ExoPlayerPlaybackRepository
import com.astheticon.mp3player.domain.repository.PlaybackRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides playback‑related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PlaybackModule {

    companion object {
        @Provides
        @Singleton
        fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer =
            ExoPlayer.Builder(context).build()
    }

    @Binds
    @Singleton
    abstract fun bindPlaybackRepository(
        impl: ExoPlayerPlaybackRepository
    ): PlaybackRepository
}
