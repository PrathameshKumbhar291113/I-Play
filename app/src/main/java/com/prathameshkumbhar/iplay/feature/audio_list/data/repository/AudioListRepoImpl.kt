package com.prathameshkumbhar.iplay.feature.audio_list.data.repository

import com.prathameshkumbhar.iplay.database.dao.AudioDao
import com.prathameshkumbhar.iplay.database.entity.AudioEntity
import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioListRepository
import javax.inject.Inject

class AudioListRepoImpl @Inject constructor(private val audioDao: AudioDao) : AudioListRepository{
    override suspend fun getAllDownloads(): List<AudioEntity>{
        return audioDao.getAllDownloads()
    }
}