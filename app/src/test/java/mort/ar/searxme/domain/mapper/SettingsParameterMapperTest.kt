package mort.ar.searxme.domain.mapper

import mort.ar.searxme.data.model.SearchInstance
import mort.ar.searxme.data.model.SettingsParameter
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges
import mort.ar.searxme.presentation.model.Categories.FILES
import mort.ar.searxme.presentation.model.Categories.IMAGES
import mort.ar.searxme.presentation.model.Engines.BING
import mort.ar.searxme.presentation.model.Engines.DUCKDUCKGO
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsParameterMapperTest {

    private val mapper = SettingsParameterMapper()

    @Test
    fun `GIVEN WHEN mapToSettingsParameter() THEN`() {
        val testedSettingsParameter = mapper.mapToSettingsParameter(
            searchInstances = listOf(favoriteSearchInstance, secondarySearchInstance),
            engines = engines,
            categories = categories,
            language = language,
            timeRange = timeRange
        )

        assertEquals(expectedSettingsParameter, testedSettingsParameter)
    }

    companion object {
        private const val favUrl = "https://searx.be"
        private val favoriteSearchInstance = SearchInstance(
            name = "https://searx.be",
            url = favUrl,
            favorite = true
        )

        private const val secUrl = "https://searx.me"
        private val secondarySearchInstance = SearchInstance(
            name = "https://searx.me",
            url = secUrl,
            favorite = false
        )

        private const val engines = "duckduckgo,bing"
        private const val categories = "files,images"
        private val language = Languages.GERMAN
        private val timeRange = TimeRanges.MONTH

        private val expectedSettingsParameter = SettingsParameter(
            searxInstances = listOf(favUrl, secUrl),
            engines = listOf(DUCKDUCKGO, BING),
            categories = listOf(FILES, IMAGES),
            language = language,
            timeRange = timeRange
        )
    }

}