package com.example.viewmodel

import android.app.Application
import android.content.Context
import com.example.timer.RoundTimer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.GameSoundEffects
import com.example.data.GameSessionStore
import com.example.data.MovieRepository
import com.example.data.NepaliGangNames
import com.example.data.OnlineFilmDirectory
import com.example.data.WikipediaPosterRepository
import com.example.model.AppLanguage
import com.example.model.GamePhase
import com.example.model.GameSettings
import com.example.model.Movie
import com.example.model.MovieLanguage
import com.example.model.RoundRecord
import com.example.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

val defaultInitialPair = NepaliGangNames.getRandomPair()

data class GameUiState(
    val phase: GamePhase = GamePhase.SPLASH,
    val appLanguage: AppLanguage = AppLanguage.NEPALI,
    val team1: Team = Team(1, defaultInitialPair.team1Nepali, 0xFFE11D48, defaultInitialPair.team1Emoji),
    val team2: Team = Team(2, defaultInitialPair.team2Nepali, 0xFF2563EB, defaultInitialPair.team2Emoji),
    val activeTeamId: Int = 1,
    val currentRoundNumber: Int = 1,
    val settings: GameSettings = GameSettings(timerDurationSeconds = 180),
    val currentMovie: Movie? = null,
    val isCardRevealed: Boolean = false,
    val remainingSeconds: Int = 180,
    val isTimerRunning: Boolean = false,
    val isTimerPaused: Boolean = false,
    val roundHistory: List<RoundRecord> = emptyList(),
    val totalMoviesCount: Int = 0,
    val isOnlineSyncing: Boolean = false,
    val onlineStatusText: String = "Connected to Movie Database",
    val roundInterrupted: Boolean = false,
    val backgroundTimerUnavailable: Boolean = false,
    val lastRoundResult: RoundRecord? = null
)

class GameViewModel @JvmOverloads constructor(application: Application, private val networkEnabled: Boolean = true) : AndroidViewModel(application) {

    val repository = MovieRepository()
    private val soundEffects = GameSoundEffects(application)

    private val store = GameSessionStore(application)
    private val saved = store.load()
    private val _uiState = MutableStateFlow(saved?.game?.let {
        if (it.phase == GamePhase.ACTING && saved?.sameBoot == false) it.copy(phase = GamePhase.READY_TURN,
            currentMovie = null, isTimerRunning = false, isTimerPaused = false, roundInterrupted = true)
        else it
    } ?: GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var movieDeck = mutableListOf<Movie>()

    init {
        soundEffects.isEnabled = _uiState.value.settings.soundEffectsEnabled
        store.loadCustom().forEach(repository::addCustomMovie)
        if (_uiState.value.phase == GamePhase.ACTING && RoundTimer.state.value.session == 0L && saved != null) {
            RoundTimer.restore(application, saved.deadline, saved.remaining, saved.running,
                _uiState.value.settings.soundEffectsEnabled, _uiState.value.appLanguage == AppLanguage.NEPALI)
        }
        if (_uiState.value.phase == GamePhase.ACTING) monitorTimer()
        viewModelScope.launch {
            _uiState.collect { state ->
                store.save(state, RoundTimer.deadline(), RoundTimer.remainingMillis(), RoundTimer.state.value.running)
            }
        }
        viewModelScope.launch { repository.customMovies.collect { store.saveCustom(it) } }
        viewModelScope.launch {
            RoundTimer.state.collect { timer ->
                if (_uiState.value.phase == GamePhase.ACTING && timer.session != 0L) {
                    _uiState.update { it.copy(remainingSeconds = timer.remainingSeconds,
                        isTimerRunning = timer.running, isTimerPaused = !timer.running && !timer.expired,
                        backgroundTimerUnavailable = timer.unavailable) }
                    if (timer.expired) onTimeUp()
                }
            }
        }

        refreshMovieCount()
        // Collect live streaming status from OnlineFilmDirectory
        viewModelScope.launch {
            OnlineFilmDirectory.streamStatus.collect { status ->
                _uiState.update { it.copy(onlineStatusText = status) }
            }
        }
        viewModelScope.launch {
            OnlineFilmDirectory.isStreaming.collect { isSyncing ->
                _uiState.update { it.copy(isOnlineSyncing = isSyncing) }
            }
        }
        viewModelScope.launch {
            OnlineFilmDirectory.onlineMovies.collect {
                refreshMovieCount()
            }
        }
        // Preload posters in background immediately
        if (networkEnabled) viewModelScope.launch(Dispatchers.IO) {
            try {
                val allMovies = repository.getAllMovies(MovieLanguage.ALL)
                allMovies.take(4).forEach { WikipediaPosterRepository.fetchPosterForMovie(it) }
                repository.fetchLimitlessOnline(MovieLanguage.ALL)
            } catch (_: Exception) {
            }
        }
    }

    fun dismissSplash() {
        soundEffects.playUiTap()
        _uiState.update {
            if (it.phase == GamePhase.SPLASH) it.copy(phase = GamePhase.SETUP) else it
        }
    }

    fun refreshMovieCount() {
        val count = repository.getMoviesForLanguages(_uiState.value.settings.selectedLanguages).size
        _uiState.update { it.copy(totalMoviesCount = count) }
    }

    fun fetchMoreOnlineFilms() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.fetchLimitlessOnline(MovieLanguage.ALL, forceMore = true)
            } finally {
                refreshMovieCount()
            }
        }
    }

    fun searchOnlineAndAdd(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val results = repository.searchOnlineDirectly(query, MovieLanguage.ALL)
                results.forEach { repository.addCustomMovie(it) }
            } finally {
                refreshMovieCount()
            }
        }
    }

    fun updateTeam1Name(name: String) {
        _uiState.update { it.copy(team1 = it.team1.copy(name = name)) }
    }

    fun updateTeam2Name(name: String) {
        _uiState.update { it.copy(team2 = it.team2.copy(name = name)) }
    }

    fun updateTeam1Color(colorHex: Long) {
        _uiState.update { it.copy(team1 = it.team1.copy(colorHex = colorHex)) }
    }

    fun updateTeam2Color(colorHex: Long) {
        _uiState.update { it.copy(team2 = it.team2.copy(colorHex = colorHex)) }
    }

    fun randomizeGangNames() {
        soundEffects.playUiTap()
        val currentLang = _uiState.value.appLanguage
        val newPair = NepaliGangNames.getRandomPair()
        _uiState.update {
            it.copy(
                team1 = it.team1.copy(name = newPair.getTeam1Name(currentLang), emoji = newPair.team1Emoji),
                team2 = it.team2.copy(name = newPair.getTeam2Name(currentLang), emoji = newPair.team2Emoji)
            )
        }
    }

    fun randomizeTeam1Name() {
        soundEffects.playUiTap()
        val currentLang = _uiState.value.appLanguage
        val single = NepaliGangNames.getRandomSingleGang(excludeName = _uiState.value.team2.name)
        _uiState.update {
            it.copy(
                team1 = it.team1.copy(name = single.getName(currentLang), emoji = single.emoji)
            )
        }
    }

    fun randomizeTeam2Name() {
        soundEffects.playUiTap()
        val currentLang = _uiState.value.appLanguage
        val single = NepaliGangNames.getRandomSingleGang(excludeName = _uiState.value.team1.name)
        _uiState.update {
            it.copy(
                team2 = it.team2.copy(name = single.getName(currentLang), emoji = single.emoji)
            )
        }
    }

    fun updateTimerDuration(seconds: Int) {
        if (seconds !in 30..600 || _uiState.value.phase == GamePhase.ACTING) return
        _uiState.update {
            it.copy(
                settings = it.settings.copy(timerDurationSeconds = seconds),
                remainingSeconds = seconds
            )
        }
    }

    fun toggleLanguageFilter(language: MovieLanguage) {
        val currentSet = _uiState.value.settings.selectedLanguages
        val newSet = if (currentSet.contains(language)) {
            if (currentSet.size > 1) currentSet - language else currentSet // Keep at least one
        } else {
            currentSet + language
        }
        _uiState.update {
            it.copy(settings = it.settings.copy(selectedLanguages = newSet))
        }
        refreshMovieCount()
    }

    fun setLanguagePreset(presetLanguages: Set<MovieLanguage>) {
        if (presetLanguages.isEmpty()) return
        _uiState.update {
            it.copy(settings = it.settings.copy(selectedLanguages = presetLanguages))
        }
        refreshMovieCount()
    }

    fun updateLanguageFilter(language: MovieLanguage) {
        setLanguagePreset(setOf(language))
    }

    fun toggleSoundEffects() {
        val newEnabled = !_uiState.value.settings.soundEffectsEnabled
        soundEffects.isEnabled = newEnabled
        RoundTimer.soundEnabled = newEnabled
        _uiState.update {
            it.copy(settings = it.settings.copy(soundEffectsEnabled = newEnabled))
        }
    }

    fun startNewGame() {
        RoundTimer.stop(getApplication())
        val currentSettings = _uiState.value.settings
        val allMovies = repository.getMoviesForLanguages(
            languages = currentSettings.selectedLanguages,
            difficulty = currentSettings.selectedDifficulty
        )

        movieDeck.clear()
        movieDeck.addAll(allMovies.shuffled())

        _uiState.update {
            it.copy(
                phase = GamePhase.READY_TURN,
                team1 = it.team1.copy(score = 0, correctCount = 0, passedCount = 0, timeOutCount = 0),
                team2 = it.team2.copy(score = 0, correctCount = 0, passedCount = 0, timeOutCount = 0),
                activeTeamId = 1,
                currentRoundNumber = 1,
                roundHistory = emptyList(),
                remainingSeconds = currentSettings.timerDurationSeconds,
                isTimerRunning = false,
                isTimerPaused = false,
                isCardRevealed = false,
                lastRoundResult = null
            )
        }

        // Proactively fetch posters for the upcoming deck and online catalog in background
        if (networkEnabled) viewModelScope.launch(Dispatchers.IO) {
            movieDeck.take(5).forEach { movie ->
                WikipediaPosterRepository.fetchPosterForMovie(movie)
            }
            repository.fetchLimitlessOnline(MovieLanguage.ALL)
        }
    }

    fun startTurn() {
        if (_uiState.value.phase != GamePhase.READY_TURN) return
        val nextMovie = drawNextMovie()
        val duration = _uiState.value.settings.timerDurationSeconds

        _uiState.update {
            it.copy(
                phase = GamePhase.ACTING,
                roundInterrupted = false,
                currentMovie = nextMovie,
                lastRoundResult = null,
                isCardRevealed = true,
                remainingSeconds = duration,
                isTimerRunning = true,
                isTimerPaused = false
            )
        }
        startTimer(duration)

        // Pre-fetch poster for this movie and next upcoming movies immediately
        if (networkEnabled) viewModelScope.launch(Dispatchers.IO) {
            WikipediaPosterRepository.fetchPosterForMovie(nextMovie)
            movieDeck.take(3).forEach { movie ->
                WikipediaPosterRepository.fetchPosterForMovie(movie)
            }
        }
    }

    fun toggleCardReveal() {
        if (_uiState.value.phase != GamePhase.ACTING) return
        _uiState.update { it.copy(isCardRevealed = !it.isCardRevealed) }
    }

    fun togglePauseTimer() {
        soundEffects.playUiTap()
        if (_uiState.value.phase != GamePhase.ACTING) return
        RoundTimer.toggle(getApplication())
    }

    fun skipCurrentMovie() {
        if (!canAct()) return
        soundEffects.playUiTap()
        val nextMovie = drawNextMovie()
        _uiState.update {
            it.copy(
                currentMovie = nextMovie,
                isCardRevealed = true
            )
        }
        if (networkEnabled) viewModelScope.launch(Dispatchers.IO) {
            WikipediaPosterRepository.fetchPosterForMovie(nextMovie)
            movieDeck.take(2).forEach { movie ->
                WikipediaPosterRepository.fetchPosterForMovie(movie)
            }
        }
    }

    fun onGuessCorrect() {
        if (!canAct()) return
        RoundTimer.stop(getApplication())
        timerJob?.cancel()
        soundEffects.playCorrectDing()
        vibrate(100)

        val currentState = _uiState.value
        val activeTeam = if (currentState.activeTeamId == 1) currentState.team1 else currentState.team2
        val movie = currentState.currentMovie ?: Movie("0", "Movie", "", 2020, MovieLanguage.HINDI_MODERN, "Drama")
        val timeUsed = currentState.settings.timerDurationSeconds - currentState.remainingSeconds

        val record = RoundRecord(
            roundNumber = currentState.currentRoundNumber,
            teamName = activeTeam.name,
            teamColor = activeTeam.colorHex,
            movie = movie,
            isCorrect = true,
            timeUsedSeconds = timeUsed
        )

        val updatedTeam1 = if (currentState.activeTeamId == 1) {
            currentState.team1.copy(
                score = currentState.team1.score + 1,
                correctCount = currentState.team1.correctCount + 1
            )
        } else currentState.team1

        val updatedTeam2 = if (currentState.activeTeamId == 2) {
            currentState.team2.copy(
                score = currentState.team2.score + 1,
                correctCount = currentState.team2.correctCount + 1
            )
        } else currentState.team2

        _uiState.update {
            it.copy(
                phase = GamePhase.ROUND_SUMMARY,
                team1 = updatedTeam1,
                team2 = updatedTeam2,
                roundHistory = it.roundHistory + record,
                lastRoundResult = record,
                isTimerRunning = false,
                isTimerPaused = false
            )
        }
        proceedToNextTurn()
    }

    fun onGuessWrongOrPass() {
        if (!canAct()) return
        RoundTimer.stop(getApplication())
        timerJob?.cancel()
        soundEffects.playWrongBuzz()
        vibrate(200)

        val currentState = _uiState.value
        val activeTeam = if (currentState.activeTeamId == 1) currentState.team1 else currentState.team2
        val movie = currentState.currentMovie ?: Movie("0", "Movie", "", 2020, MovieLanguage.HINDI_MODERN, "Drama")
        val timeUsed = currentState.settings.timerDurationSeconds - currentState.remainingSeconds

        val record = RoundRecord(
            roundNumber = currentState.currentRoundNumber,
            teamName = activeTeam.name,
            teamColor = activeTeam.colorHex,
            movie = movie,
            isCorrect = false,
            timeUsedSeconds = timeUsed
        )

        val updatedTeam1 = if (currentState.activeTeamId == 1) {
            currentState.team1.copy(passedCount = currentState.team1.passedCount + 1)
        } else currentState.team1

        val updatedTeam2 = if (currentState.activeTeamId == 2) {
            currentState.team2.copy(passedCount = currentState.team2.passedCount + 1)
        } else currentState.team2

        _uiState.update {
            it.copy(
                phase = GamePhase.ROUND_SUMMARY,
                team1 = updatedTeam1,
                team2 = updatedTeam2,
                roundHistory = it.roundHistory + record,
                lastRoundResult = record,
                isTimerRunning = false,
                isTimerPaused = false
            )
        }
        proceedToNextTurn()
    }

    private fun onTimeUp() {
        if (_uiState.value.phase != GamePhase.ACTING) return
        vibrate(600)

        val currentState = _uiState.value
        val activeTeam = if (currentState.activeTeamId == 1) currentState.team1 else currentState.team2
        val movie = currentState.currentMovie ?: Movie("0", "Movie", "", 2020, MovieLanguage.HINDI_MODERN, "Drama")

        val record = RoundRecord(
            roundNumber = currentState.currentRoundNumber,
            teamName = activeTeam.name,
            teamColor = activeTeam.colorHex,
            movie = movie,
            isCorrect = false,
            timeUsedSeconds = currentState.settings.timerDurationSeconds
        )

        val updatedTeam1 = if (currentState.activeTeamId == 1) {
            currentState.team1.copy(timeOutCount = currentState.team1.timeOutCount + 1)
        } else currentState.team1

        val updatedTeam2 = if (currentState.activeTeamId == 2) {
            currentState.team2.copy(timeOutCount = currentState.team2.timeOutCount + 1)
        } else currentState.team2

        _uiState.update {
            it.copy(
                phase = GamePhase.ROUND_SUMMARY,
                team1 = updatedTeam1,
                team2 = updatedTeam2,
                roundHistory = it.roundHistory + record,
                lastRoundResult = record,
                remainingSeconds = 0,
                isTimerRunning = false,
                isTimerPaused = false
            )
        }
        proceedToNextTurn()
    }

    fun proceedToNextTurn() {
        if (_uiState.value.phase != GamePhase.ROUND_SUMMARY) return
        val nextTeamId = if (_uiState.value.activeTeamId == 1) 2 else 1
        val nextRound = if (nextTeamId == 1) _uiState.value.currentRoundNumber + 1 else _uiState.value.currentRoundNumber

        _uiState.update {
            it.copy(
                phase = GamePhase.READY_TURN,
                activeTeamId = nextTeamId,
                currentRoundNumber = nextRound,
                remainingSeconds = it.settings.timerDurationSeconds,
                isCardRevealed = false,
                currentMovie = null
            )
        }
    }

    fun concludeGame() {
        RoundTimer.stop(getApplication())
        timerJob?.cancel()
        soundEffects.playWinnerFanfare()
        vibrate(300)
        _uiState.update {
            it.copy(
                phase = GamePhase.WINNER_PAGE,
                isTimerRunning = false,
                isTimerPaused = false
            )
        }
    }

    fun returnToSetup() {
        RoundTimer.stop(getApplication())
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                phase = GamePhase.SETUP,
                isTimerRunning = false,
                isTimerPaused = false
            )
        }
    }

    private fun canAct(): Boolean {
        if (_uiState.value.phase != GamePhase.ACTING || _uiState.value.isTimerPaused) return false
        return RoundTimer.tick().let { it.running && !it.expired }
    }

    private fun startTimer(durationSeconds: Int) {
        RoundTimer.start(getApplication(), durationSeconds, _uiState.value.settings.soundEffectsEnabled,
            _uiState.value.appLanguage == AppLanguage.NEPALI)
        monitorTimer()
    }

    private fun monitorTimer() {
        // Fallback also keeps visible gameplay correct if the OS rejects service startup.
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.phase == GamePhase.ACTING) { RoundTimer.tick(); delay(100) }
        }
    }

    private fun drawNextMovie(): Movie {
        if (networkEnabled && movieDeck.size < 8) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.fetchLimitlessOnline(_uiState.value.settings.selectedLanguage)
            }
        }

        if (movieDeck.isEmpty()) {
            val all = repository.getMoviesForLanguages(_uiState.value.settings.selectedLanguages, _uiState.value.settings.selectedDifficulty)
            movieDeck.addAll(all.shuffled())
        }

        val drawn = movieDeck.removeFirstOrNull() ?: Movie(
            id = "default_3idiots",
            title = "3 Idiots",
            nativeTitle = "३ इडियट्स",
            year = 2009,
            language = MovieLanguage.HINDI_MODERN,
            genre = "Comedy"
        )
        return drawn
    }

    fun setAppLanguage(language: AppLanguage) {
        _uiState.update { current ->
            if (current.appLanguage == language) return@update current
            
            val newTeam1Name = NepaliGangNames.translateName(current.team1.name, language)
            val newTeam2Name = NepaliGangNames.translateName(current.team2.name, language)
                              
            current.copy(
                appLanguage = language,
                team1 = current.team1.copy(name = newTeam1Name),
                team2 = current.team2.copy(name = newTeam2Name)
            )
        }
    }

    fun toggleAppLanguage() {
        val next = if (_uiState.value.appLanguage == AppLanguage.ENGLISH) AppLanguage.NEPALI else AppLanguage.ENGLISH
        setAppLanguage(next)
    }

    private fun vibrate(millis: Long) {
        try {
            val context = getApplication<Application>().applicationContext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(millis)
                }
            }
        } catch (_: Exception) {
            // Ignore vibration errors on non-vibrating devices
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundEffects.close()
    }
}

