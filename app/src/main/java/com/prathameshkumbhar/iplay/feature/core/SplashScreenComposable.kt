package com.prathameshkumbhar.iplay.feature.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.prathameshkumbhar.iplay.R
import com.prathameshkumbhar.iplay.feature.audio_list.presentation.AudioListComposable
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable


@Serializable
object SplashScreenComposable

@Composable
fun SplashScreenComposable(navHostController: NavHostController) {

    LaunchedEffect(Unit) {
        delay(2000)
        navHostController.navigate(AudioListComposable) {
            popUpTo(SplashScreenComposable) { inclusive = true }
        }
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        val (logo) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.iplay_logo),
            contentDescription = "I Play",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .constrainAs(logo) {
                    centerTo(parent)
                    width = Dimension.value(350.dp)
                    height = Dimension.ratio("1:1")
                }
        )
    }
}