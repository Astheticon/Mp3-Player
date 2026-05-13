# Mp3-Player 🎵

A modern local offline MP3 music player for Android 
 built with Kotlin, Jetpack Compose, and Media3 ExoPlayer.

## Screenshots
(Add screenshots here)

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose (no XML)
- **Audio Engine:** Media3 ExoPlayer
- **Architecture:** Single Activity + ViewModel
- **State Management:** Compose mutableStateOf
- **Theme:** Material3 Dark

## Features
### Phase 1 ✅
- Select and play MP3 files from device storage
- Play / Pause controls
- Seek bar with live position tracking
- Current time and total duration display
- Runtime permission handling (API 31+)

### Phase 2 ✅
- Auto-scan device for all audio files
- Music library view with song list
- Background playback
- Media notification controls
- Lock screen controls
- Next / Previous track
- Shuffle and Repeat modes
- Navigation between Library and Player screens

### Phase 3 🔜
- Song metadata (title, artist, album)
- Album artwork display
- Persist last played song and position

### Phase 4 🔜
- Search functionality
- Sort by name, artist, duration
- Favorites system
- Recently played list
- Playback speed control

## Requirements
- Android 7.0+ (API 24)
- READ_EXTERNAL_STORAGE (API 31 and below)
- READ_MEDIA_AUDIO (API 33 and above)

## Project Structure
app/src/main/java/com/example/musicplayer/
├── MainActivity.kt
├── PlayerViewModel.kt
├── model/
│   └── Song.kt
├── service/
│   └── PlaybackService.kt
├── ui/
│   ├── LibraryScreen.kt
│   ├── PlayerScreen.kt
│   ├── components/
│   │   └── SongItem.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── utils/
    ├── MediaScanner.kt
    ├── PermissionHelper.kt
    └── TimeFormatter.kt

## Setup
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Run on device or emulator (API 24+)

## License
MIT License
