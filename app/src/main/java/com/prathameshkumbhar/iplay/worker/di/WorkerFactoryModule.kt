package com.prathameshkumbhar.iplay.worker.di

import androidx.work.WorkerFactory
import com.prathameshkumbhar.iplay.worker.factory.HiltWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WorkerFactoryModule {

    @Provides
    fun provideWorkerFactory(
    ): WorkerFactory {
        return HiltWorkerFactory()
    }
}