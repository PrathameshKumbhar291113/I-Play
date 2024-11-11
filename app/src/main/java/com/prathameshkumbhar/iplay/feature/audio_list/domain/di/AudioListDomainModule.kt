package com.prathameshkumbhar.iplay.feature.audio_list.domain.di

import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioListRepository
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.AudioListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioListDomainModule {

    @Provides
    @Singleton
    fun providesAudioListUseCase(audioListRepository: AudioListRepository): AudioListUseCase{
        return AudioListUseCase(audioListRepository)
    }

}