package com.prathameshkumbhar.iplay.feature.audio_list.data.repository

import com.prathameshkumbhar.iplay.R
import com.prathameshkumbhar.iplay.database.model.AudioFile
import javax.inject.Inject

class CreateAudioFilesRepository @Inject constructor() {
    fun getCreatedAudioFiles(): List<AudioFile> {
        return listOf(
            AudioFile(
                1, "Love Again", R.raw.dua_lipa_love_again,
                R.drawable.love_again_poster.toString(), false
            ),
            AudioFile(
                2, "Levitating", R.raw.dua_lipa_levitating,
                R.drawable.levitating_poster.toString(), false
            ),
        )
    }
}