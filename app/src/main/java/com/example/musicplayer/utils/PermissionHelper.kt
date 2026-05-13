package com.example.musicplayer.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionHelper {
    val AUDIO_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    fun isGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            AUDIO_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
