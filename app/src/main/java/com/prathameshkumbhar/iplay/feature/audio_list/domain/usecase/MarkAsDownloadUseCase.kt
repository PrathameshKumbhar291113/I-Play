package com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase

import com.prathameshkumbhar.iplay.feature.audio_list.domain.repository.AudioRepository
import javax.inject.Inject

class MarkAsDownloadUseCase @Inject constructor(
    private val audioRepository: AudioRepository
) {
    suspend operator fun invoke(id: Int){
        audioRepository.markAsDownloaded(id)
    }
}