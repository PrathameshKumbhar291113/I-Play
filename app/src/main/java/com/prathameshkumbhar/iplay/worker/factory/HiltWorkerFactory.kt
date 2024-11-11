package com.prathameshkumbhar.iplay.worker.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.prathameshkumbhar.iplay.worker.DownloadAudioWorker
import javax.inject.Inject

class HiltWorkerFactory @Inject constructor(
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParams: WorkerParameters
    ): ListenableWorker? {
        return if (workerClassName == DownloadAudioWorker::class.java.name) {
            DownloadAudioWorker(appContext, workerParams)
        } else {
            null
        }
    }
}
