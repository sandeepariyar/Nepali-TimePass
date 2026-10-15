package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.model.GamePhase
import com.example.ui.screens.ActingScreen
import com.example.ui.screens.ReadyTurnScreen
import com.example.ui.screens.RoundSummaryScreen
import com.example.ui.screens.SetupScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WinnerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameViewModel
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Setup Coil ImageLoader with valid User-Agent for Wikipedia/Wikimedia images
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "MovieCharadesApp/1.0 (Android; charades_game@gmail.com) Mozilla/5.0 (Linux; Android 14)")
                    .build()
                chain.proceed(request)
            }
            .build()

        val imageLoader = ImageLoader.Builder(this)
            .okHttpClient(okHttpClient)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50 * 1024 * 1024)
                    .build()
            }
            .crossfade(true)
            .build()

        Coil.setImageLoader(imageLoader)

        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameAppContent(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GameAppContent(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState.phase,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "phase_transition",
        modifier = modifier.fillMaxSize()
    ) { phase ->
        when (phase) {
            GamePhase.SPLASH -> {
                SplashScreen(
                    onContinue = viewModel::dismissSplash
                )
            }

            GamePhase.SETUP -> {
                SetupScreen(
                    uiState = uiState,
                    onToggleAppLanguage = viewModel::toggleAppLanguage,
                    onSetAppLanguage = viewModel::setAppLanguage,
                    onTeam1NameChange = viewModel::updateTeam1Name,
                    onTeam2NameChange = viewModel::updateTeam2Name,
                    onTeam1ColorChange = viewModel::updateTeam1Color,
                    onTeam2ColorChange = viewModel::updateTeam2Color,
                    onRandomizeGangs = viewModel::randomizeGangNames,
                    onRandomizeTeam1 = viewModel::randomizeTeam1Name,
                    onRandomizeTeam2 = viewModel::randomizeTeam2Name,
                    onTimerDurationChange = viewModel::updateTimerDuration,
                    onToggleLanguage = viewModel::toggleLanguageFilter,
                    onSetLanguagePreset = viewModel::setLanguagePreset,
                    onToggleSound = viewModel::toggleSoundEffects,
                    onStartGame = viewModel::startNewGame,
                    onFetchMoreOnline = viewModel::fetchMoreOnlineFilms,
                    onSearchOnlineAndAdd = viewModel::searchOnlineAndAdd,
                    onAddCustomMovie = { movie ->
                        viewModel.repository.addCustomMovie(movie)
                        viewModel.refreshMovieCount()
                    }
                )
            }

            GamePhase.READY_TURN -> {
                ReadyTurnScreen(
                    uiState = uiState,
                    onStartTurn = viewModel::startTurn,
                    onConcludeGame = viewModel::concludeGame,
                    onBackToSetup = viewModel::returnToSetup
                )
            }

            GamePhase.ACTING -> {
                ActingScreen(
                    uiState = uiState,
                    onToggleReveal = viewModel::toggleCardReveal,
                    onTogglePauseTimer = viewModel::togglePauseTimer,
                    onGuessCorrect = viewModel::onGuessCorrect,
                    onGuessWrongOrPass = viewModel::onGuessWrongOrPass,
                    onSkipMovie = viewModel::skipCurrentMovie,
                    onConcludeGame = viewModel::concludeGame
                )
            }

            GamePhase.ROUND_SUMMARY -> {
                RoundSummaryScreen(
                    uiState = uiState,
                    onNextTurn = viewModel::proceedToNextTurn,
                    onConcludeGame = viewModel::concludeGame
                )
            }

            GamePhase.WINNER_PAGE -> {
                WinnerScreen(
                    uiState = uiState,
                    onPlayAgain = viewModel::startNewGame,
                    onReturnToSetup = viewModel::returnToSetup
                )
            }
        }
    }
}
