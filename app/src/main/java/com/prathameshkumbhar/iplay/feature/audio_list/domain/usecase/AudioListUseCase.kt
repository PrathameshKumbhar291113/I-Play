package com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase

import com.prathameshkumbhar.iplay.database.entity.AudioEntity
import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AudioListUseCase @Inject constructor(private val audioListRepository: AudioListRepository) {
    operator fun invoke(): Flow<List<AudioEntity>> =  flow<List<AudioEntity>> {
        if (audioListRepository.getAllDownloads().isNotEmpty()){
            emit(audioListRepository.getAllDownloads())
        }else{
            emit(emptyList())
        }
    }
}