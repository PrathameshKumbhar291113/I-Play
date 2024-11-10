package com.prathameshkumbhar.iplay.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prathameshkumbhar.iplay.database.dao.AudioDao
import com.prathameshkumbhar.iplay.database.entity.AudioEntity
import com.prathameshkumbhar.iplay.utils.IPlayRoomDbConstants.I_PLAY_DB

@Database(entities = [AudioEntity::class], version = 1, exportSchema = false)
abstract class IPlayDatabase : RoomDatabase() {
    companion object {
        fun getInstance(context: Context): IPlayDatabase {
            return Room.databaseBuilder(context, IPlayDatabase::class.java, I_PLAY_DB)
                .fallbackToDestructiveMigration().build()
        }

    }

    abstract fun audioDao(): AudioDao
}