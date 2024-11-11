package com.prathameshkumbhar.iplay.utils

import android.annotation.SuppressLint
import android.content.Context
import com.prathameshkumbhar.iplay.R

@SuppressLint("DiscouragedApi")
fun String.toResId(context: Context): Int {
    return try {
        val id = context.resources.getIdentifier(this, "drawable", context.packageName)
        id
    } catch (e: Exception) {
        R.drawable.iplay_logo
    }
}

fun formatTime(millis: Long): String {
    val minutes = (millis / 1000) / 60
    val seconds = (millis / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}