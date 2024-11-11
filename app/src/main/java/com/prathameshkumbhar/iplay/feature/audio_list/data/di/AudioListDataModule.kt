package com.prathameshkumbhar.iplay.feature.audio_list.data.di

import com.prathameshkumbhar.iplay.database.dao.AudioDao
import com.prathameshkumbhar.iplay.feature.audio_list.data.repository.AudioListRepoImpl
import com.prathameshkumbhar.iplay.feature.audio_list.data.repository.CreateAudioFilesRepository
import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioListDataModule {

    @Provides
    @Singleton
    fun providesAudioListRepoImpl(audioDao: AudioDao): AudioListRepository{
        return AudioListRepoImpl(audioDao)
    }

    @Provides
    @Singleton
    fun providesCreateAudioFilesRepository(): CreateAudioFilesRepository{
        return CreateAudioFilesRepository()
    }

}