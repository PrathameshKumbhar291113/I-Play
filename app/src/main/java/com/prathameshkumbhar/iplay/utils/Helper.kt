package com.prathameshkumbhar.iplay.utils

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("DiscouragedApi")
fun String?.toResId(context: Context, defaultResId: Int): Int {
    return this?.let {
        try {
            val resId = context.resources.getIdentifier(it, "drawable", context.packageName)
            if (resId != 0) resId else defaultResId
        } catch (e: Exception) {
            defaultResId
        }
    } ?: defaultResId
}

@SuppressLint("DiscouragedApi")
fun String?.toRawResId(context: Context, defaultResId: Int): Int {
    return this?.let {
        try {
            val resId = context.resources.getIdentifier(it, "raw", context.packageName)
            if (resId != 0) resId else defaultResId
        } catch (e: Exception) {
            defaultResId
        }
    } ?: defaultResId
}

fun formatTime(millis: Long): String {
    val minutes = (millis / 1000) / 60
    val seconds = (millis / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}