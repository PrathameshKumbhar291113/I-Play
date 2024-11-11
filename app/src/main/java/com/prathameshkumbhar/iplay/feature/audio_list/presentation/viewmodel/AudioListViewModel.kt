package com.prathameshkumbhar.iplay.feature.audio_list.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.prathameshkumbhar.iplay.database.dto.toAudioEntity
import com.prathameshkumbhar.iplay.database.entity.AudioEntity
import com.prathameshkumbhar.iplay.database.model.AudioFile
import com.prathameshkumbhar.iplay.feature.audio_list.data.repository.CreateAudioFilesRepository
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.GetDownloadedAudioListUseCase
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.InsertAudioUseCase
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.MarkAsDownloadUseCase
import com.prathameshkumbhar.iplay.feature.audio_list.domain.usecase.UpdatePlayBackPositionUseCase
import com.prathameshkumbhar.iplay.service.NetworkMonitorService
import com.prathameshkumbhar.iplay.worker.DownloadAudioWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioListViewModel @Inject constructor(
    private val createAudioFilesRepository: CreateAudioFilesRepository,
    private val networkMonitor: NetworkMonitorService,
    private val workManager: WorkManager,
    private val audioDownloadedListUseCase: GetDownloadedAudioListUseCase,
    private val insertAudioUseCase: InsertAudioUseCase,
    private val markAsDownloadUseCase: MarkAsDownloadUseCase,
    private val updatePlayBackPositionUseCase: UpdatePlayBackPositionUseCase
) : ViewModel() {

    private val _audioFiles = MutableStateFlow<List<AudioFile>>(emptyList())
    val audioFiles: StateFlow<List<AudioFile>> = _audioFiles

    private val isDownloadInProgress = MutableStateFlow(false)

    var isNetworkAvailable = MutableStateFlow<Boolean>(false)

    private val _downloadedAudioFiles = MutableStateFlow<List<AudioEntity>>(emptyList())
    val downloadedAudioFiles: StateFlow<List<AudioEntity>> = _downloadedAudioFiles

    private val _downloadStatusMap = MutableStateFlow<Map<Int, String>>(emptyMap())
    val downloadStatusMap: StateFlow<Map<Int, String>> = _downloadStatusMap

    private fun updateDownloadStatus(audioId: Int, status: String) {
        _downloadStatusMap.value = _downloadStatusMap.value.toMutableMap().apply {
            this[audioId] = status
        }
    }

    init {
        networkMonitor.isNetworkAvailable.observeForever { isAvailable ->
            loadAudioFilesBasedOnNetwork(isAvailable)
            isNetworkAvailable.value = isAvailable
        }
    }

    private fun loadAudioFilesBasedOnNetwork(isAvailable: Boolean) {
        if (isAvailable) {
            val createdAudioFiles = createAudioFilesRepository.getCreatedAudioFiles()
            _audioFiles.value = createdAudioFiles
            createdAudioFiles.forEach { audioFile ->
                insertAudioFile(audioFile.toAudioEntity())
            }
        } else {
            getDownloadedAudioFiles()
        }
    }

    private fun insertAudioFile(audioFile: AudioEntity) {
        insertAudioUseCase(audioFile).launchIn(viewModelScope)
    }

    private fun markAsDownloaded(audioId: Int) {
        viewModelScope.launch {
            markAsDownloadUseCase(audioId)
        }
    }

    fun updatePlaybackPosition(audioId: Int, position: Long, progress: Float) {
        updatePlayBackPositionUseCase(audioId, position, progress).launchIn(viewModelScope)
    }

    private fun getDownloadedAudioFiles() {
        audioDownloadedListUseCase().onEach { downloadedFiles ->
            Log.e("I_PLAY_DB", "getDownloadedAudioFiles: ${downloadedFiles.toList().toString()}", )
            _downloadedAudioFiles.value = downloadedFiles
        }.launchIn(viewModelScope)
    }

    fun downloadAudio(audioFileId: String, songName: String, audioId: Int) {
        if (isDownloadInProgress.value) {
            updateDownloadStatus(audioId, "Another download is in progress")
            return
        }

        isDownloadInProgress.value = true
        val workRequestTag = "download_$audioFileId"
        val existingWork = workManager.getWorkInfosByTag(workRequestTag).get().firstOrNull()

        if (existingWork != null && (existingWork.state == WorkInfo.State.RUNNING || existingWork.state == WorkInfo.State.ENQUEUED)) {
            updateDownloadStatus(audioId, "Downloading already in progress")
            return
        }

        val inputData = Data.Builder()
            .putString("audioFileId", audioFileId)
            .putString("songName", songName)
            .build()

        val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadAudioWorker>()
            .setInputData(inputData)
            .addTag(workRequestTag)
            .build()

        workManager.enqueue(downloadWorkRequest)

        workManager.getWorkInfoByIdLiveData(downloadWorkRequest.id).observeForever { workInfo ->
            when (workInfo.state) {
                WorkInfo.State.ENQUEUED -> updateDownloadStatus(audioId, "Downloading queued")
                WorkInfo.State.RUNNING -> {
                    updateDownloadStatus(audioId, "Downloading...")
                }

                WorkInfo.State.SUCCEEDED -> {
                    updateDownloadStatus(audioId, "Download complete")
                    isDownloadInProgress.value = false
                    markAsDownloaded(audioId)
                }

                WorkInfo.State.FAILED -> {
                    updateDownloadStatus(audioId, "Download failed")
                    isDownloadInProgress.value = false
                }

                else -> {}
            }
        }
    }
}

