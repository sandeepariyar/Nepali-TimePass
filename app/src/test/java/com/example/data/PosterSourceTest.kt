package com.example.data

import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PosterSourceTest {
    @Test fun pageImagesUsesRequestedThumbnailBeforeOriginal() {
        val data = JSONObject("""{"thumbnail":{"source":"https://example.org/1024.jpg"},"original":{"source":"https://example.org/full.jpg"}}""")
        assertEquals("https://example.org/1024.jpg", WikipediaPosterRepository.selectPosterSource(data))
    }
    @Test fun summaryPrefersOriginalAndEmptyThumbnailDoesNotHideFallback() {
        val data = JSONObject("""{"thumbnail":{"source":""},"originalimage":{"source":"https://example.org/poster.jpg"}}""")
        assertEquals("https://example.org/poster.jpg", WikipediaPosterRepository.selectPosterSource(data, true))
        assertEquals("https://example.org/poster.jpg", WikipediaPosterRepository.selectPosterSource(data))
    }
    @Test fun missingOrInvalidSourcesKeepFallbackUi() {
        assertNull(WikipediaPosterRepository.selectPosterSource(JSONObject("""{"thumbnail":{"source":"javascript:bad"}}""")))
        assertNull(WikipediaPosterRepository.selectPosterSource(JSONObject()))
    }
}
