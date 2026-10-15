package com.example.data

import android.util.Log
import com.example.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object WikipediaPosterRepository {
    private const val TAG = "PosterRepo"

    private val client = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    // Cache: movieId -> posterUrl
    private val cache = ConcurrentHashMap<String, String>()

    // Observable poster URLs state map for Composables
    private val _posterUrls = MutableStateFlow<Map<String, String>>(emptyMap())
    val posterUrls: StateFlow<Map<String, String>> = _posterUrls.asStateFlow()

    // Pre-mapped Wikipedia titles for 100% accurate dynamic poster discovery
    private val wikiTitleMap = mapOf(
        "hi_3idiots" to "3_Idiots",
        "hi_sholay" to "Sholay",
        "hi_ddlj" to "Dilwale_Dulhania_Le_Jayenge",
        "hi_herapheri" to "Hera_Pheri_(2000_film)",
        "hi_dangal" to "Dangal_(film)",
        "hi_k3g" to "Kabhi_Khushi_Kabhie_Gham...",
        "hi_lagaan" to "Lagaan",
        "hi_pk" to "PK_(film)",
        "hi_stree" to "Stree_(2018_film)",
        "hi_jawan" to "Jawan_(film)",
        "hi_pathaan" to "Pathaan_(film)",
        "hi_gangs_wasseypur" to "Gangs_of_Wasseypur",
        "hi_munnabhai" to "Munna_Bhai_M.B.B.S.",
        "hi_andhadhun" to "Andhadhun",
        "hi_znmd" to "Zindagi_Na_Milegi_Dobara",
        "hi_drishyam" to "Drishyam_(2015_film)",
        "hi_barfi" to "Barfi!",
        "hi_queen" to "Queen_(2014_film)",
        "hi_yjhd" to "Yeh_Jawaani_Hai_Deewani",
        "hi_animal" to "Animal_(2023_film)",
        "hi_bahubali" to "Baahubali:_The_Beginning",
        "hi_kantrara" to "Kantara_(film)",
        "hi_rrr" to "RRR",
        "hi_chakde" to "Chak_De!_India",
        "hi_kaho_naa_pyaar_hai" to "Kaho_Naa..._Pyaar_Hai",
        "hi_golmaal" to "Golmaal:_Fun_Unlimited",
        "hi_taare_zameen_par" to "Taare_Zameen_Par",
        "hi_bajrangi_bhaijaan" to "Bajrangi_Bhaijaan",
        "hi_swades" to "Swades",
        "ne_purna_bahadur" to "Purna_Bahadur_Ko_Sarangi",
        "ne_loot" to "Loot_(2012_film)",
        "ne_loot_2" to "Loot_2",
        "ne_kabaddi" to "Kabaddi_(2014_film)",
        "ne_kabaddi_kabaddi" to "Kabaddi_Kabaddi",
        "ne_kabaddi_3" to "Kabaddi_Kabaddi_Kabaddi",
        "ne_kabaddi_4" to "Kabaddi_4:_The_Final_Match",
        "ne_chhakka_panja" to "Chhakka_Panja",
        "ne_chhakka_panja_2" to "Chhakka_Panja_2",
        "ne_chhakka_panja_3" to "Chhakka_Panja_3",
        "ne_chhakka_panja_4" to "Chhakka_Panja_4",
        "ne_chhakka_panja_5" to "Chhakka_Panja_5",
        "ne_darpan_chhaya" to "Darpan_Chhaya",
        "ne_pashupati_prasad" to "Pashupati_Prasad",
        "ne_bhasme_don" to "Pashupati_Prasad_2:_Bhasme_Don",
        "ne_jatra" to "Jatra_(2016_film)",
        "ne_jatrai_jatra" to "Jatrai_Jatra",
        "ne_mahajatra" to "Mahajatra",
        "ne_prem_geet" to "Prem_Geet_(2016_film)",
        "ne_prem_geet_2" to "Prem_Geet_2",
        "ne_prem_geet_3" to "Prem_Geet_3",
        "ne_jaari" to "Jaari_(2023_film)",
        "ne_mahapurush" to "Mahapurush_(2022_film)",
        "ne_kalo_pothi" to "Kalo_Pothi",
        "ne_boksi_ko_ghar" to "Boksi_Ko_Ghar",
        "ne_dimag_kharab" to "Dimag_Kharab",
        "ne_kusume_rumal" to "Kusume_Rumal_(1985_film)",
        "ne_lahure" to "Lahure_(film)",
        "ne_hostel" to "Hostel_(2013_film)",
        "ne_a_mero_hajur" to "A_Mero_Hajur",
        "ne_a_mero_hajur_2" to "A_Mero_Hajur_2",
        "ne_a_mero_hajur_3" to "A_Mero_Hajur_3",
        "ne_a_mero_hajur_4" to "A_Mero_Hajur_4",
        "ne_kohinoor" to "Kohinoor_(2014_film)",
        "ne_jerryy" to "Jerryy",
        "ne_dreams" to "Dreams_(2016_film)",
        "ne_gajalu" to "Gajalu",
        "ne_captain" to "Captain_(2019_film)",
        "ne_fulbari" to "Fulbari_(film)",
        "ne_dui_numbari" to "Dui_Numbari",
        "ne_shatru_gate" to "Shatru_Gate",
        "ne_bulbul" to "Bulbul_(2019_film)",
        "ne_prasad" to "Prasad_(2018_film)",
        "ne_ghampani" to "Ghampani",
        "ne_sano_sansar" to "Sano_Sansar",
        "ne_kagbeni" to "Kagbeni_(film)",
        "ne_seto_surya" to "White_Sun_(film)"
    )

    init {
        val initialMap = mutableMapOf<String, String>()
        MovieDatabase.movies.forEach { movie ->
            if (movie.posterUrl.isNotEmpty()) {
                cache[movie.id] = movie.posterUrl
                initialMap[movie.id] = movie.posterUrl
            }
        }
        _posterUrls.value = initialMap
    }

    fun getCachedPoster(movieId: String): String? = cache[movieId]

    suspend fun preloadAllPosters(movies: List<Movie>) = withContext(Dispatchers.IO) {
        movies.forEach { movie ->
            fetchPosterForMovie(movie)
        }
    }

    suspend fun fetchPosterForMovie(movie: Movie): String? = withContext(Dispatchers.IO) {
        // If already cached and valid, return it
        val existing = cache[movie.id]
        if (!existing.isNullOrEmpty() && existing.startsWith("http")) {
            return@withContext existing
        }

        val slugCandidates = listOfNotNull(
            wikiTitleMap[movie.id],
            "${cleanTitle(movie.title)}_(film)",
            "${cleanTitle(movie.title)}_(${movie.year}_film)",
            cleanTitle(movie.title)
        )

        for (slug in slugCandidates) {
            // Strategy 1: Wikipedia REST API summary
            var url = tryFetchWikipediaSummary(slug)
            
            // Strategy 2: MediaWiki pageimages action API
            if (url.isNullOrEmpty()) {
                url = tryFetchMediaWikiPageImages(slug)
            }

            // Strategy 3: MediaWiki generator=images prop=imageinfo (extracts all images on page)
            if (url.isNullOrEmpty()) {
                url = tryFetchMediaWikiImages(slug)
            }

            if (!url.isNullOrEmpty()) {
                cache[movie.id] = url
                _posterUrls.value = cache.toMap()
                return@withContext url
            }
        }

        // Fallback to database URL if any
        if (movie.posterUrl.isNotEmpty()) {
            cache[movie.id] = movie.posterUrl
            _posterUrls.value = cache.toMap()
            return@withContext movie.posterUrl
        }

        null
    }

    private fun cleanTitle(title: String): String {
        return title
            .replace(" (DDLJ)", "")
            .replace(" (The Black Hen)", "")
            .replace(" (The Witch's House)", "")
            .trim()
            .replace(" ", "_")
    }

    private fun tryFetchWikipediaSummary(slug: String): String? {
        return try {
            val encoded = URLEncoder.encode(slug, "UTF-8").replace("+", "%20")
            val request = Request.Builder()
                .url("https://en.wikipedia.org/api/rest_v1/page/summary/$encoded")
                .header("User-Agent", "MovieCharadesApp/1.0 (Android; charades_game@gmail.com) Mozilla/5.0")
                .header("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                val json = JSONObject(body)
                val thumbnail = json.optJSONObject("thumbnail")
                val originalImage = json.optJSONObject("originalimage")

                val source = thumbnail?.optString("source")
                    ?: originalImage?.optString("source")

                if (!source.isNullOrEmpty() && source.startsWith("http")) {
                    source
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun tryFetchMediaWikiPageImages(slug: String): String? {
        return try {
            val encoded = URLEncoder.encode(slug, "UTF-8").replace("+", "%20")
            val url = "https://en.wikipedia.org/w/api.php?action=query&titles=$encoded&prop=pageimages&format=json&pithumbsize=600"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "MovieCharadesApp/1.0 (Android; charades_game@gmail.com) Mozilla/5.0")
                .header("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                val json = JSONObject(body)
                val query = json.optJSONObject("query") ?: return null
                val pages = query.optJSONObject("pages") ?: return null

                val keys = pages.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val pageObj = pages.optJSONObject(key) ?: continue
                    val thumbnail = pageObj.optJSONObject("thumbnail")
                    val source = thumbnail?.optString("source")
                    if (!source.isNullOrEmpty() && source.startsWith("http")) {
                        return source
                    }
                }
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun tryFetchMediaWikiImages(slug: String): String? {
        return try {
            val encoded = URLEncoder.encode(slug, "UTF-8").replace("+", "%20")
            val url = "https://en.wikipedia.org/w/api.php?action=query&titles=$encoded&generator=images&gimlimit=10&prop=imageinfo&iiprop=url&format=json"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "MovieCharadesApp/1.0 (Android; charades_game@gmail.com) Mozilla/5.0")
                .header("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                val json = JSONObject(body)
                val query = json.optJSONObject("query") ?: return null
                val pages = query.optJSONObject("pages") ?: return null

                val keys = pages.keys()
                var candidateUrl: String? = null
                while (keys.hasNext()) {
                    val key = keys.next()
                    val pageObj = pages.optJSONObject(key) ?: continue
                    val title = pageObj.optString("title", "").lowercase()
                    
                    // Skip system icons
                    if (title.contains("commons-logo") || title.contains("edit-clear") ||
                        title.contains("wikiquote") || title.contains("question_book") ||
                        title.contains("padlock") || title.contains("symbol") || title.contains("flag")
                    ) {
                        continue
                    }

                    val imageInfoArray = pageObj.optJSONArray("imageinfo") ?: continue
                    if (imageInfoArray.length() > 0) {
                        val info = imageInfoArray.getJSONObject(0)
                        val imgUrl = info.optString("url")
                        if (!imgUrl.isNullOrEmpty() && imgUrl.startsWith("http")) {
                            if (title.contains("poster") || title.contains("cover") || title.contains("film") || title.contains("movie")) {
                                return imgUrl // Highest priority match
                            }
                            if (candidateUrl == null && (imgUrl.endsWith(".jpg") || imgUrl.endsWith(".jpeg") || imgUrl.endsWith(".png"))) {
                                candidateUrl = imgUrl
                            }
                        }
                    }
                }
                candidateUrl
            }
        } catch (e: Exception) {
            null
        }
    }
}
