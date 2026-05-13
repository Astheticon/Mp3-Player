@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.musicplayer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.PlayerViewModel
import com.example.musicplayer.ui.components.SongItem

@Composable
fun LibraryScreen(viewModel: PlayerViewModel, onSongClick: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadLibrary(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Library") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        if (viewModel.songs.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicOff,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )
                Text("No songs found", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(top = 16.dp))
                Text("Add MP3 files to your device storage", color = Color.DarkGray, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Text(
                    text = "${viewModel.songs.size} songs",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.songs) { song ->
                        SongItem(
                            song = song,
                            isPlaying = viewModel.songs.getOrNull(viewModel.currentSongIndex) == song && viewModel.isPlaying,
                            onClick = {
                                viewModel.loadSong(viewModel.songs.indexOf(song))
                                onSongClick()
                            }
                        )
                    }
                }
            }
        }
    }
}
