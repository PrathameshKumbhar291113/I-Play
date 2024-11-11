package com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase

import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdatePlayBackPositionUseCase @Inject constructor(
    private val audioRepository: AudioRepository
) {
    operator fun invoke(id: Int, position: Long, progress: Float): Flow<Unit> = flow<Unit> {
        audioRepository.updatePlaybackPosition(id, position, progress)
        emit(Unit)
    }
}