package com.prathameshkumbhar.iplay.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prathameshkumbhar.iplay.database.entity.AudioEntity

@Dao
interface AudioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudio(audioEntity: AudioEntity)

    @Query("SELECT * FROM audio_table WHERE isDownloaded = 1")
    suspend fun getDownloadedAudio(): List<AudioEntity>

    @Query("UPDATE audio_table SET currentPosition = :position, progress = :progress WHERE id = :id")
    suspend fun updatePlaybackPosition(id: Int, position: Long, progress: Float)

    @Query("UPDATE audio_table SET isDownloaded = 1 WHERE id = :id")
    suspend fun markAsDownloaded(id: Int)

}