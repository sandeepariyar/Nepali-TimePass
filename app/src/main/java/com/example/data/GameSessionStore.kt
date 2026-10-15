package com.example.data

import android.content.Context
import android.provider.Settings
import com.example.model.Movie
import com.example.viewmodel.GameUiState
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject

/** A single preference transaction keeps the game and monotonic checkpoint together. */
class GameSessionStore(context: Context) {
    private val prefs = context.getSharedPreferences("game_session_v1", Context.MODE_PRIVATE)
    private val resolver = context.contentResolver
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val gameAdapter = moshi.adapter(GameUiState::class.java)
    private val movieAdapter = moshi.adapter<List<Movie>>(Types.newParameterizedType(List::class.java, Movie::class.java))
    private fun bootCount(): Int = Settings.Global.getInt(resolver, Settings.Global.BOOT_COUNT, -1)
    data class Saved(val game: GameUiState, val deadline: Long, val remaining: Long, val running: Boolean, val sameBoot: Boolean)
    fun load(): Saved? = try {
        val json = JSONObject(prefs.getString("session", null) ?: "{}")
        val game = gameAdapter.fromJson(json.getString("game"))
        game?.let { Saved(it, json.optLong("deadline"), json.optLong("remaining"), json.optBoolean("running"),
            json.optInt("boot", -2) == bootCount() && bootCount() >= 0) }
    } catch (_: Exception) { null }
    fun save(game: GameUiState, deadline: Long, remaining: Long, running: Boolean) {
        val json = JSONObject().put("game", gameAdapter.toJson(game)).put("deadline", deadline)
            .put("remaining", remaining).put("running", running).put("boot", bootCount())
        prefs.edit().putString("session", json.toString()).apply()
    }
    fun loadCustom(): List<Movie> = try { movieAdapter.fromJson(prefs.getString("custom", "[]")!!) ?: emptyList() } catch (_: Exception) { emptyList() }
    fun saveCustom(movies: List<Movie>) { prefs.edit().putString("custom", movieAdapter.toJson(movies)).apply() }
}
