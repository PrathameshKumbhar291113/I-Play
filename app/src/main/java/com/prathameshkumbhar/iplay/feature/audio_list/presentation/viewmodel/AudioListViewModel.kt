package com.prathameshkumbhar.iplay.feature.audio_list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.prathameshkumbhar.iplay.database.model.AudioFile
import com.prathameshkumbhar.iplay.feature.audio_list.data.repository.CreateAudioFilesRepository
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.AudioListUseCase
import com.prathameshkumbhar.iplay.worker.DownloadAudioWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AudioListViewModel @Inject constructor(
    private val createAudioFilesRepository: CreateAudioFilesRepository,
    private val audioListUseCase: AudioListUseCase,
    private val workManager: WorkManager
) : ViewModel(){

    private val _audioFiles = MutableStateFlow<List<AudioFile>>(emptyList())
    val audioFiles: StateFlow<List<AudioFile>> = _audioFiles

    private val _downloadStatus = MutableStateFlow<String>("")
    val downloadStatus: StateFlow<String> = _downloadStatus

    init {
        fetchAudioFiles()
    }

    private fun fetchAudioFiles() {
        _audioFiles.value = createAudioFilesRepository.getCreatedAudioFiles()
    }

    fun downloadAudio(audioFileId: String, songName: String, audioId: Int) {
        val inputData = Data.Builder()
            .putString("audioFileId", audioFileId)
            .putString("songName", songName)
            .build()

        val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadAudioWorker>()
            .setInputData(inputData)
            .build()

        workManager.enqueue(downloadWorkRequest)
        workManager.getWorkInfoByIdLiveData(downloadWorkRequest.id).observeForever { workInfo ->
            when (workInfo.state) {
                WorkInfo.State.ENQUEUED -> {
                    _downloadStatus.value = "Download queued"
                }
                WorkInfo.State.RUNNING -> {
                    val progress = workInfo.progress.getInt("Progress", 0)
                    _downloadStatus.value = "Downloading... $progress%"
                }
                WorkInfo.State.SUCCEEDED -> {
                    _downloadStatus.value = "Download complete"
                }
                WorkInfo.State.FAILED -> {
                    _downloadStatus.value = "Download failed"
                }
                else -> {
                }
            }
        }
    }

}