package com.prathameshkumbhar.iplay.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prathameshkumbhar.iplay.database.entity.AudioEntity

@Dao
interface AudioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudio(audioEntity: AudioEntity)

    @Update
    suspend fun updateAudio(audioEntity: AudioEntity)

    @Query("SELECT * FROM audio_table WHERE id = :id")
    suspend fun getDownloadById(id: Int): AudioEntity?

    @Query("SELECT * FROM audio_table")
    suspend fun getAllDownloads(): List<AudioEntity>

    @Query("SELECT * FROM audio_table WHERE isDownloaded = 1")
    fun getDownloadedAudio(): LiveData<List<AudioEntity>>

    @Query("UPDATE audio_table SET progress = :progress, isDownloaded = :isDownloaded WHERE id = :id")
    suspend fun updateProgress(id: Int, progress: Float, isDownloaded: Boolean)

}