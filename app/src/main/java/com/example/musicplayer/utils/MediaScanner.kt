package com.example.musicplayer.utils

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.musicplayer.model.Song

object MediaScanner {
    fun scanForAudio(context: Context): List<Song> {
        val songs = mutableListOf<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        context.contentResolver.query(uri, projection, selection, null, sortOrder)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {
                val duration = cursor.getLong(durCol)
                if (duration < 30000L) continue

                val id = cursor.getLong(idCol)
                val title = cursor.getString(titleCol) ?: Song.UNKNOWN
                val artist = cursor.getString(artistCol) ?: Song.UNKNOWN
                val album = cursor.getString(albumCol) ?: Song.UNKNOWN
                val albumId = cursor.getLong(albumIdCol)
                val contentUri = ContentUris.withAppendedId(uri, id)

                songs.add(Song(id, title, artist, album, duration, contentUri, albumId))
            }
        }
        return songs
    }
}
