package com.prathameshkumbhar.iplay.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prathameshkumbhar.iplay.utils.IPlayRoomDbConstants.AUDIO_TABLE

@Entity(tableName = AUDIO_TABLE)
data class AudioEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val filePath: String,
    val progress: Float,
    val isDownloaded: Boolean = false
)