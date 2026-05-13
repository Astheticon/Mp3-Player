package com.example.musicplayer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.ui.LibraryScreen
import com.example.musicplayer.ui.PlayerScreen
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import com.example.musicplayer.utils.PermissionHelper

object Routes {
    const val LIBRARY = "library"
    const val PLAYER = "player"
}

class MainActivity : ComponentActivity() {
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) renderScreen()
        else Toast.makeText(this, "Storage permission required", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PermissionHelper.isGranted(this)) renderScreen()
        else permissionLauncher.launch(PermissionHelper.AUDIO_PERMISSION)
    }

    private fun renderScreen() {
        setContent {
            val viewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.factory(this))
            val filePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri -> uri?.let { viewModel.loadFile(it, this@MainActivity) } }
            
            LaunchedEffect(Unit) {
                while(true) {
                    viewModel.updateProgress()
                    kotlinx.coroutines.delay(500)
                }
            }

            val navController = rememberNavController()

            startService(android.content.Intent(this@MainActivity, com.example.musicplayer.service.PlaybackService::class.java))

            MusicPlayerTheme {
                NavHost(navController = navController, startDestination = Routes.LIBRARY) {
                    composable(Routes.LIBRARY) {
                        BackHandler(enabled = true) {}
                        LibraryScreen(
                            viewModel = viewModel,
                            onSongClick = { navController.navigate(Routes.PLAYER) }
                        )
                    }
                    composable(Routes.PLAYER) {
                        PlayerScreen(
                            viewModel = viewModel,
                            onFilePick = { filePickerLauncher.launch("audio/*") },
                            onNavigateBack = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
