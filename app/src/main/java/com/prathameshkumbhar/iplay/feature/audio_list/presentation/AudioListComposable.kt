package com.prathameshkumbhar.iplay.feature.audio_list.presentation

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.prathameshkumbhar.iplay.R
import com.prathameshkumbhar.iplay.database.model.AudioFile
import com.prathameshkumbhar.iplay.feature.audio_list.presentation.viewmodel.AudioListViewModel
import com.prathameshkumbhar.iplay.utils.formatTime
import kotlinx.serialization.Serializable

@Serializable
object AudioListComposable

@OptIn(UnstableApi::class)
@Composable
fun AudioListComposable() {

    val audioListViewModel: AudioListViewModel = hiltViewModel()
    val audioList = audioListViewModel.audioFiles.collectAsState()
    val context = LocalContext.current
    val playingStateMap = remember { mutableStateMapOf<Int, Boolean>() }
    var currentlyPlaying by remember { mutableStateOf<AudioFile?>(null) }
    var progress by remember { mutableFloatStateOf(0f) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (titleLogo, list, bottomBar) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.iv_iplay),
            contentDescription = "iPlay Logo",
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 30.dp)
                .constrainAs(titleLogo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 25.dp)
        )

        LazyColumn(
            modifier = Modifier
                .constrainAs(list) {
                    top.linkTo(titleLogo.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomBar.top)
                }
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(audioList.value) { index, audioFile ->
                val isPlaying = playingStateMap[audioFile.audioFileId] ?: false

                audioFile.audioFileId?.let { audioFileId ->
                    AudioFileItem(
                        audioFile = audioFile,
                        context = context,
                        isPlaying = isPlaying,
                        onPlayPauseToggle = { playing ->
                            playingStateMap[audioFileId] = playing
                            if (playing) {
                                currentlyPlaying = audioFile
                                GlobalPlayer.player?.play()
                            } else if (currentlyPlaying?.audioFileId == audioFileId) {
                                currentlyPlaying = null
                                GlobalPlayer.player?.pause()
                            }

                            audioList.value.forEachIndexed { i, file ->
                                file.audioFileId?.let { fileId ->
                                    if (i != index) playingStateMap[fileId] = false
                                }
                            }
                        },
                        updateProgress = { updatedProgress, currentPos, songDuration ->
                            progress = updatedProgress
                            currentPosition = currentPos
                            duration = songDuration
                        },
                        audioListViewModel,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }


        ConstraintLayout(
            modifier = Modifier
                .constrainAs(bottomBar) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            val (songTitle, progressBar, controls) = createRefs()


            Text(
                text = currentlyPlaying?.songTitle ?: "Nothing is playing",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(songTitle) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = 8.dp)
            )

                Row(
                    modifier = Modifier
                        .constrainAs(progressBar) {
                            top.linkTo(songTitle.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        style = MaterialTheme.typography.bodySmall
                    )
                    LinearProgressIndicator(
                        progress = {
                            progress.coerceIn(0f, 1f)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = formatTime(duration),
                        style = MaterialTheme.typography.bodySmall
                    )
                }


                Row(
                    modifier = Modifier
                        .constrainAs(controls) {
                            top.linkTo(progressBar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = {
                        currentlyPlaying?.audioFileId?.let {
                            playingStateMap[it] = false
                            currentlyPlaying = null
                            GlobalPlayer.player?.pause()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.iv_pause),
                            contentDescription = "Pause"
                        )
                    }

                    IconButton(onClick = {
                        currentlyPlaying?.audioFileId?.let {
                            playingStateMap[it] = true
                            GlobalPlayer.player?.play()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.iv_play),
                            contentDescription = "Play"
                        )
                    }
                }

        }
    }
}


@Preview
@Composable
fun AudioListComposablePreview(){
    AudioListComposable()
}
