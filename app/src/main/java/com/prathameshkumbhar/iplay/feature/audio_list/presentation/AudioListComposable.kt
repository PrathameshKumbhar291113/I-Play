package com.prathameshkumbhar.iplay.feature.audio_list.presentation

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.prathameshkumbhar.iplay.R
import com.prathameshkumbhar.iplay.database.dto.toAudioFile
import com.prathameshkumbhar.iplay.database.model.AudioFile
import com.prathameshkumbhar.iplay.feature.audio_list.presentation.viewmodel.AudioListViewModel
import com.prathameshkumbhar.iplay.utils.formatTime
import com.prathameshkumbhar.iplay.utils.toRawResId
import com.prathameshkumbhar.iplay.utils.toResId
import kotlinx.serialization.Serializable

@Serializable
object AudioListComposable

@OptIn(UnstableApi::class)
@Composable
fun AudioListComposable() {
    val audioListViewModel: AudioListViewModel = hiltViewModel()
    val audioListState = audioListViewModel.audioFiles.collectAsState()
    val downloadedAudioFilesState = audioListViewModel.downloadedAudioFiles.collectAsState()
    val context = LocalContext.current
    val playingStateMap = remember { mutableStateMapOf<Int, Boolean>() }
    var currentlyPlaying by remember { mutableStateOf<AudioFile?>(null) }
    var progress by remember { mutableFloatStateOf(0f) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var songPosterImage by remember { mutableStateOf("") }
    val downloadedAudioFiles = downloadedAudioFilesState.value.map { it.toAudioFile() }
    val isNetworkAvailable by audioListViewModel.isNetworkAvailable.collectAsState()

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

            val filteredAudioFiles = if (isNetworkAvailable) {
                audioListState.value
            } else {
                downloadedAudioFiles
            }


            itemsIndexed(filteredAudioFiles) { index, audioFile ->
                val isPlaying = playingStateMap[audioFile.audioFileId.toRawResId(
                    context,
                    R.raw.dua_lipa_levitating
                )] ?: false
                audioFile.audioFileId?.toRawResId(context, R.raw.dua_lipa_levitating)
                    ?.let { audioFileId ->
                        AudioFileItem(
                            audioFile = audioFile,
                            context = context,
                            isPlaying = isPlaying,
                            onPlayPauseToggle = { playing ->
                                playingStateMap[audioFileId] = playing
                                if (playing) {
                                    currentlyPlaying = audioFile
                                    GlobalPlayer.player?.play()
                                } else if (currentlyPlaying?.audioFileId.toRawResId(
                                        context,
                                        R.raw.dua_lipa_levitating
                                    ) == audioFileId
                                ) {
                                    currentlyPlaying = null
                                    GlobalPlayer.player?.pause()
                                }

                                filteredAudioFiles.forEachIndexed { i, file ->
                                    file.audioFileId?.toRawResId(context, R.raw.dua_lipa_levitating)
                                        ?.let { fileId ->
                                            if (i != index) playingStateMap[fileId] = false
                                        }
                                }
                            },
                            updateProgress = { updatedProgress, currentPos, songDuration, imageFileName, audioId ->
                                progress = updatedProgress
                                currentPosition = currentPos
                                duration = songDuration
                                songPosterImage = imageFileName
                                audioListViewModel.updatePlaybackPosition(
                                    audioId,
                                    currentPosition,
                                    progress
                                )
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
            val (songPoster, songTitle, progressBar) = createRefs()

            val posterResourceId = if (!currentlyPlaying?.songTitle.isNullOrEmpty()) {
                songPosterImage.toResId(context, R.drawable.iplay_logo)
            } else {
                R.drawable.iplay_logo
            }

            Image(
                painter = painterResource(id = posterResourceId),
                contentDescription = "song poster small image",
                modifier = Modifier
                    .padding(start = 55.dp, bottom = 15.dp)
                    .constrainAs(songPoster) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(songTitle.start)
                    }
                    .size(50.dp)
                    .clip(RoundedCornerShape(15.dp))
            )

            Text(
                text = currentlyPlaying?.songTitle ?: "Nothing is playing",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                color = colorResource(id = R.color.red),
                modifier = Modifier
                    .padding(bottom = 15.dp, end = 55.dp)
                    .constrainAs(songTitle) {
                        top.linkTo(songPoster.top)
                        start.linkTo(songPoster.end)
                        end.linkTo(parent.end)
                        centerVerticallyTo(songPoster)
                    }
                    .padding(start = 4.dp)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .constrainAs(progressBar) {
                        top.linkTo(songTitle.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTime(currentPosition),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(id = R.color.red)
                )
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = colorResource(id = R.color.purple),
                )
                Text(
                    text = formatTime(duration),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(id = R.color.red)
                )
            }
        }

        if (isNetworkAvailable){
            Toast.makeText(context, "Auto Sync Started, Connected to Internet.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Auto Sync Stopped, No Internet.", Toast.LENGTH_SHORT).show()
        }
    }
}


@Preview
@Composable
fun AudioListComposablePreview() {
    AudioListComposable()
}
