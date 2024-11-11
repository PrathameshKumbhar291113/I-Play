package com.prathameshkumbhar.iplay.feature.audio_list.domain.repository

import com.prathameshkumbhar.iplay.database.entity.AudioEntity

interface AudioListRepository {
    suspend fun getAllDownloads(): List<AudioEntity>
}