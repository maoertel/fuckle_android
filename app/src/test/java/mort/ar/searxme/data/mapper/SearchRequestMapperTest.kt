package mort.ar.searxme.data.mapper

import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchRequestMapperTest {

    private val mapper = SearchRequestMapper()

    @Test
    fun `GIVEN everything not empty WHEN mapToSearchRequest() THEN SearchRequest mapped`() {
        val searchRequest = SearchRequest(
            query,
            categories,
            engines,
            "de",
            pageNo,
            "month",
            format,
            imageProxy,
            autoComplete,
            safeSearch
        )

        val testedSearchRequest = mapper.mapToSearchRequest(
            query,
            categories,
            engines,
            language,
            pageNo,
            timeRange,
            format,
            imageProxy,
            autoComplete,
            safeSearch
        )

        assertEquals(searchRequest, testedSearchRequest)
    }

    @Test
    fun `GIVEN everything empty WHEN mapToSearchRequest() THEN SearchRequest mapped`() {
        val expectedSearchRequest = SearchRequest(query, null, null, null, 1, null, null, null, null, null)

        val testedSearchRequest = mapper.mapToSearchRequest(query, "", "", null, 1, null, "", "", "", "")

        assertEquals(expectedSearchRequest, testedSearchRequest)
    }

    @Test
    fun `GIVEN everything null WHEN mapToSearchRequest() THEN SearchRequest mapped`() {
        val expectedSearchRequest = SearchRequest(query, null, null, null, 1, null, null, null, null, null)

        val testedSearchRequest = mapper.mapToSearchRequest(query, null, null, null, 1, null, null, null, null, null)

        assertEquals(expectedSearchRequest, testedSearchRequest)
    }

    companion object {
        const val query = "hello"
        const val categories = "it"
        const val engines = "bing,ddg"
        val language = Languages.GERMAN
        const val pageNo = 1
        val timeRange = TimeRanges.MONTH
        const val format = "bla"
        val imageProxy = null
        const val autoComplete: String = "ddg"
        const val safeSearch: String = "true"
    }

}