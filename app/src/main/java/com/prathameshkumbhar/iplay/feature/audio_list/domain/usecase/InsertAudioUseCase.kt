package com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase

import com.prathameshkumbhar.iplay.database.entity.AudioEntity
import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertAudioUseCase @Inject constructor(private val audioListRepository: AudioRepository) {
    operator fun invoke(audioEntity: AudioEntity): Flow<Unit> = flow {
        audioListRepository.insertAudio(audioEntity)
        emit(Unit)
    }
}