package com.prathameshkumbhar.iplay.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prathameshkumbhar.iplay.database.entity.AudioEntity

@Dao
interface AudioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(audioDownloadEntity: AudioEntity)

    @Update
    suspend fun updateDownload(audioDownloadEntity: AudioEntity)

    @Query("SELECT * FROM audio_table WHERE id = :id")
    suspend fun getDownloadById(id: Int): AudioEntity?

    @Query("SELECT * FROM audio_table")
    suspend fun getAllDownloads(): List<AudioEntity>

}