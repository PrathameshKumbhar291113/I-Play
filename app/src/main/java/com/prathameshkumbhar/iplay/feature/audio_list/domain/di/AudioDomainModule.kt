package com.prathameshkumbhar.iplay.feature.audio_list.domain.di

import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioRepository
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.GetDownloadedAudioListUseCase
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.InsertAudioUseCase
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.MarkAsDownloadUseCase
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.UpdatePlayBackPositionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioDomainModule {

    @Provides
    @Singleton
    fun providesDownloadedAudioListUseCase(audioRepository: AudioRepository): GetDownloadedAudioListUseCase{
        return GetDownloadedAudioListUseCase(audioRepository)
    }

    @Provides
    @Singleton
    fun providesInsertAudioUseCase(audioRepository: AudioRepository): InsertAudioUseCase{
        return InsertAudioUseCase(audioRepository)
    }

    @Provides
    @Singleton
    fun providesMarkAsDownloadUseCase(audioRepository: AudioRepository): MarkAsDownloadUseCase {
        return MarkAsDownloadUseCase(audioRepository)
    }

    @Provides
    @Singleton
    fun providesUpdatePlayBackPositionUseCase(audioRepository: AudioRepository): UpdatePlayBackPositionUseCase {
        return UpdatePlayBackPositionUseCase(audioRepository)
    }

}