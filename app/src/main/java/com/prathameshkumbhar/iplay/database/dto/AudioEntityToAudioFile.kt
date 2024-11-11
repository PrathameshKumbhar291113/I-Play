package com.prathameshkumbhar.iplay.database.dto

import com.prathameshkumbhar.iplay.database.entity.AudioEntity
import com.prathameshkumbhar.iplay.database.model.AudioFile

fun AudioEntity.toAudioFile(): AudioFile {
    return AudioFile(
        id = this.id,
        songTitle = this.songTitle,
        audioFileId = this.audioFileId.toString(),
        imageFileName = this.imageFileName,
        isDownloaded = this.isDownloaded,
        isAutoSyncComplete = this.isAutoSyncComplete,
    )
}