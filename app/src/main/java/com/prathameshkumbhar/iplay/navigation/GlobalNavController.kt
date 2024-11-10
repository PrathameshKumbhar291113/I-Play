package com.prathameshkumbhar.iplay.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prathameshkumbhar.iplay.feature.audio_detail.presentation.AudioDetailComposable
import com.prathameshkumbhar.iplay.feature.audio_list.presentation.AudioListComposable
import com.prathameshkumbhar.iplay.feature.core.SplashScreenComposable

@Composable
fun GlobalNavController(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = SplashScreenComposable
    ) {
        composable<SplashScreenComposable> {
            SplashScreenComposable(navHostController = navController)
        }
        composable<AudioListComposable>{
            AudioListComposable(navHostController = navController)
        }
        composable<AudioDetailComposable>{
            AudioDetailComposable()
        }
    }
}