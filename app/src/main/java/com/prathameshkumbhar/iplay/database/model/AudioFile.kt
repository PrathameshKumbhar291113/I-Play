package com.prathameshkumbhar.iplay.database.model

data class AudioFile(
    val id: Int,
    val songTitle: String,
    val audioFileId: String? = null,
    val imageFileName: String,
    val isDownloaded: Boolean,
    val isAutoSyncComplete: Boolean
)
