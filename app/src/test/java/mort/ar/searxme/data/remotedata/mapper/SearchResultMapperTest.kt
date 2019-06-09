package mort.ar.searxme.data.remotedata.mapper

import mort.ar.searxme.data.remotedata.model.SearxResult
import org.junit.Assert.*
import org.junit.Test

class SearchResultMapperTest {

    private val mapper = SearchResultMapper()

    @Test
    fun `GIVEN all properties are non null WHEN mapFromSearxResult() called THEN return search result`() {
        val testSearchResult = mapper.mapFromSearxResult(searxResult)

        assertEquals(title, testSearchResult.title)
        assertEquals(url, testSearchResult.url)
        assertEquals(prettyUrl, testSearchResult.prettyUrl)
        assertEquals(content, testSearchResult.content)
        assertEquals(engines, testSearchResult.engines)
    }

    @Test
    fun `GIVEN content is null WHEN mapFromSearxResult() called THEN return search result with empty content property`() {
        val testSearchResult = mapper.mapFromSearxResult(searxResult.copy(content = null))

        assertEquals(title, testSearchResult.title)
        assertEquals(url, testSearchResult.url)
        assertEquals(prettyUrl, testSearchResult.prettyUrl)
        assertNotEquals(content, testSearchResult.content)
        assertTrue(testSearchResult.content.isBlank())
        assertEquals(engines, testSearchResult.engines)
    }

    companion object {
        private const val title = "title"
        private const val url = "url"
        private const val prettyUrl = "pretty"
        private const val content = "content"
        private val engines = listOf("engine")
        val searxResult = SearxResult(
            title = title,
            url = url,
            prettyUrl = prettyUrl,
            content = content,
            engines = engines,
            engine = "engine",
            category = "string",
            positions = emptyList(),
            parsed_url = emptyList(),
            score = 1.0
        )
    }

}