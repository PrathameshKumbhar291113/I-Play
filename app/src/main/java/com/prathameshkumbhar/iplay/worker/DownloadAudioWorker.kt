package com.prathameshkumbhar.iplay.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.prathameshkumbhar.iplay.MainActivity
import com.prathameshkumbhar.iplay.R
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@HiltWorker
class DownloadAudioWorker @AssistedInject constructor(
    @ApplicationContext context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @SuppressLint("DiscouragedApi")
    override suspend fun doWork(): Result {
        val audioFileId = inputData.getString("audioFileId") ?: return Result.failure()
        val songName = inputData.getString("songName") ?: return Result.failure()
        lateinit var result: Result

        kotlin.runCatching {
            val rawAudioResourceId = applicationContext.resources.getIdentifier(
                audioFileId,
                "raw",
                applicationContext.packageName
            )

            if (rawAudioResourceId == 0) {
                return Result.failure()
            }

            val inputStream = applicationContext.resources.openRawResource(rawAudioResourceId)
            val totalSize = withContext(Dispatchers.IO) {
                inputStream.available()
            }

            val outputStream = withContext(Dispatchers.IO) {
                getOutputStream(songName)
            } ?: return Result.failure()

            val buffer = ByteArray(1024)
            var length: Int
            var downloadedSize = 0
            var lastProgress = 0
            val notificationUpdateInterval = 500L
            var lastUpdateTime = System.currentTimeMillis()

            showDownloadProgressNotification(0, applicationContext, songName)

            while (withContext(Dispatchers.IO) {
                    inputStream.read(buffer)
                }.also { length = it } != -1) {


                withContext(Dispatchers.IO) {
                    outputStream.write(buffer, 0, length)
                }

                downloadedSize += length
                val progress = (downloadedSize * 100 / totalSize)
                val currentTime = System.currentTimeMillis()

                if (progress != lastProgress && currentTime - lastUpdateTime >= notificationUpdateInterval) {
                    showDownloadProgressNotification(progress, applicationContext, songName)
                    lastProgress = progress
                    lastUpdateTime = currentTime
                }

                if (progress == 100) {
                    break
                }
                delay(100)
            }

            withContext(Dispatchers.IO) {
                inputStream.close()
                outputStream.close()
            }
            showDownloadProgressNotification(100, applicationContext, songName)

        }.onSuccess {
            result = Result.success()
        }.onFailure {
            result = Result.failure()
        }
        return result
    }

    private fun getOutputStream(songName: String): FileOutputStream? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = applicationContext.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$songName.mp3")
                put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "Android/media/com.prathameshkumbhar.iplay"
                )
            }

            val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                return resolver.openOutputStream(it) as FileOutputStream
            }
        } else {
            val mediaDir = applicationContext.externalMediaDirs.firstOrNull()?.let {
                File(it, "iplaysongs").apply { mkdirs() }
            }

            val outputFile = File(mediaDir ?: applicationContext.filesDir, "$songName.mp3")
            if (!outputFile.exists()) {
                outputFile.createNewFile()
            }
            return FileOutputStream(outputFile)
        }
    }

    private fun showDownloadProgressNotification(
        progress: Int,
        context: Context,
        songName: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "download_channel"
        val channelName = "I Play Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Downloading $songName")
            .setContentText(if (progress < 100) "Downloading Progress: $progress%" else "Download Complete")
            .setSmallIcon(R.drawable.iv_download)
            .setProgress(100, progress, false)
            .setContentIntent(pendingIntent)
            .setOngoing(progress < 100)

        notificationManager.notify(1, builder.build())
    }
}
