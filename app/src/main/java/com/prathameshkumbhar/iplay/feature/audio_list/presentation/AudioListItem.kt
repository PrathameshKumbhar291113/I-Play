package com.prathameshkumbhar.iplay.feature.audio_list.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer
import com.prathameshkumbhar.iplay.R
import com.prathameshkumbhar.iplay.database.model.AudioFile
import com.prathameshkumbhar.iplay.feature.audio_list.presentation.viewmodel.AudioListViewModel
import com.prathameshkumbhar.iplay.utils.formatTime
import com.prathameshkumbhar.iplay.utils.toResId
import kotlinx.coroutines.delay


object GlobalPlayer {
    @SuppressLint("UnsafeOptInUsageError")
    var player: SimpleExoPlayer? = null
}

@OptIn(UnstableApi::class)
@Composable
fun AudioFileItem(
    audioFile: AudioFile,
    context: Context,
    isPlaying: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    updateProgress: (Float, Long, Long) -> Unit,
    audioListViewModel: AudioListViewModel,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    val player = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/${audioFile.audioFileId}")
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            if (GlobalPlayer.player != null && GlobalPlayer.player != player) {
                GlobalPlayer.player?.pause()
            }
            GlobalPlayer.player = player
            player.play()

            while (isPlaying) {
                if (player.duration > 0) {
                    currentPosition = player.currentPosition
                    duration = player.duration
                    progress = currentPosition.toFloat() / duration.toFloat()
                    updateProgress(progress, currentPosition, duration)

                } else {
                    progress = 0f
                }
                delay(100)
            }
        } else {
            player.pause()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = audioFile.imageFileName.toResId(context)),
                contentDescription = "Song Poster",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = audioFile.songTitle,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    IconButton(onClick = {
                        onPlayPauseToggle(!isPlaying)
                    }) {
                        val iconRes = if (isPlaying) {
                            R.drawable.iv_pause
                        } else {
                            R.drawable.iv_play
                        }
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Play/Pause"
                        )
                    }

                    IconButton(onClick = {
                        audioListViewModel.downloadAudio(audioFile.audioFileId.toString(), audioFile.songTitle, audioFile.id)
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.iv_download),
                            contentDescription = "Download"
                        )
                    }
                }

                if (isPlaying) {

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = formatTime(currentPosition),
                            style = MaterialTheme.typography.bodySmall
                        )
                        LinearProgressIndicator(
                            progress = { progress },
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
                }
            }
        }
    }
}