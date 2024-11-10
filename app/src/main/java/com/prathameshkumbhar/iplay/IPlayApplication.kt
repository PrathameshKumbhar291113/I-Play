package com.prathameshkumbhar.iplay

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.prathameshkumbhar.iplay.service.NetworkMonitorService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class IPlayApplication(): Application() {

    @Inject
    lateinit var networkMonitorService: NetworkMonitorService

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(networkMonitorService)
    }
}