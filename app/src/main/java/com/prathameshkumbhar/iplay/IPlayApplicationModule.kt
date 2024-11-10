package com.prathameshkumbhar.iplay

import android.content.Context
import com.prathameshkumbhar.iplay.database.IPlayDatabase
import com.prathameshkumbhar.iplay.database.dao.AudioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IPlayApplicationModule {

    @Singleton
    @Provides
    fun provideGDCareDb(@ApplicationContext context: Context): IPlayDatabase {
        return IPlayDatabase.getInstance(context)
    }

    @Provides
    fun provideUserDao(db: IPlayDatabase): AudioDao {
        return db.audioDao()
    }


}