package com.prathameshkumbhar.iplay.database.model

data class AudioFile(
    val id: Int,
    val title: String,
    val audioFileName: String,
    val imageFileName: String,
    val isDownloaded: Boolean = false
)
