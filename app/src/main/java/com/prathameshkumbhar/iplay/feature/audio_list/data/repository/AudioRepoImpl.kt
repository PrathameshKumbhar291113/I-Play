package com.prathameshkumbhar.iplay.feature.audio_list.data.repository

import com.prathameshkumbhar.iplay.database.dao.AudioDao
import com.prathameshkumbhar.iplay.database.entity.AudioEntity
import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioRepository
import javax.inject.Inject

class AudioRepoImpl @Inject constructor(private val audioDao: AudioDao) : AudioRepository{

    override suspend fun getDownloadedAudio(): List<AudioEntity> {
        return audioDao.getDownloadedAudio()
    }

    override suspend fun updatePlaybackPosition(id: Int, position: Long, progress: Float) {
        audioDao.updatePlaybackPosition(id, position, progress)
    }

    override suspend fun markAsDownloaded(id: Int) {
        audioDao.markAsDownloaded(id)
    }

    override suspend fun insertAudio(audioEntity: AudioEntity) {
        audioDao.insertAudio(audioEntity)
    }
}