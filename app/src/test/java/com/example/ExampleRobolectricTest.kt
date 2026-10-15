package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MovieDatabase
import com.example.data.NepaliGangNames
import com.example.model.MovieLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Nepali Timepass", appName)
  }

  @Test
  fun `movie database contains blockbusters mix and nepali films`() {
    val allMovies = MovieDatabase.movies
    assertTrue(allMovies.isNotEmpty())

    val blockbusterMovies = MovieDatabase.getFilteredMovies(MovieLanguage.ALL)
    assertTrue(blockbusterMovies.isNotEmpty())

    val nepaliMovies = MovieDatabase.getFilteredMovies(MovieLanguage.NEPALI)
    assertTrue(nepaliMovies.isNotEmpty())
  }

  @Test
  fun `nepali gang names contain 34 pairs and randomizer works`() {
    assertEquals(34, NepaliGangNames.PAIRS.size)
    val randomPair = NepaliGangNames.getRandomPair()
    assertNotNull(randomPair)
    assertTrue(randomPair.team1Name.isNotEmpty())
    assertTrue(randomPair.team2Name.isNotEmpty())

    val randomSingle = NepaliGangNames.getRandomSingleGang()
    assertNotNull(randomSingle)
    assertTrue(randomSingle.name.isNotEmpty())
  }
}
