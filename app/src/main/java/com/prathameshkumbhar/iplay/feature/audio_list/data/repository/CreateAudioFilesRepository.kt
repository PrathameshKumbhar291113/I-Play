package com.prathameshkumbhar.iplay.feature.audio_list.data.repository

import com.prathameshkumbhar.iplay.R
import com.prathameshkumbhar.iplay.database.model.AudioFile
import javax.inject.Inject

class CreateAudioFilesRepository @Inject constructor() {
    fun getCreatedAudioFiles(): List<AudioFile> {
        return listOf(
            AudioFile(
                1, "Love Again - Dua Lipa", R.raw.dua_lipa_love_again.toString(),
                R.drawable.love_again_poster.toString(), false
            ),
            AudioFile(
                2, "Levitating - Dua Lipa", R.raw.dua_lipa_levitating.toString(),
                R.drawable.levitating_poster.toString(), false
            ),
            AudioFile(
                3, "Animals - Martin Garrix", R.raw.martin_garrix_animals.toString(),
                R.drawable.animals_poster.toString(), false
            ),
            AudioFile(
                4, "Blinding Lights - The Weekend", R.raw.the_weekend_blinding_lights.toString(),
                R.drawable.blinding_lights_poster.toString(), false
            ),

        )
    }
}