package com.prathameshkumbhar.iplay.feature.audio_list.domain.repository

import com.prathameshkumbhar.iplay.database.entity.AudioEntity

interface AudioRepository {
    suspend fun getDownloadedAudio(): List<AudioEntity>
    suspend fun updatePlaybackPosition(id: Int, position: Long, progress: Float)
    suspend fun markAsDownloaded(id: Int)
    suspend fun insertAudio(audioEntity: AudioEntity)
}