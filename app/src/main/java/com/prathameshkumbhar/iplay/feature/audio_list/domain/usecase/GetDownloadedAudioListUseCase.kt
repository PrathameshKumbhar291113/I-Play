package com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase

import com.prathameshkumbhar.iplay.database.entity.AudioEntity
import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDownloadedAudioListUseCase @Inject constructor(private val audioListRepository: AudioRepository) {
    operator fun invoke(): Flow<List<AudioEntity>> = flow<List<AudioEntity>> {
        if (audioListRepository.getDownloadedAudio().isNotEmpty()) {
            emit(audioListRepository.getDownloadedAudio())
        } else {
            emit(emptyList<AudioEntity>())
        }
    }
}