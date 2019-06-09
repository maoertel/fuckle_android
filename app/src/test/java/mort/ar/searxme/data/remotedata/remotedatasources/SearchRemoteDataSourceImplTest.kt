package mort.ar.searxme.data.remotedata.remotedatasources

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.model.SearchResult
import mort.ar.searxme.data.remotedata.mapper.SearchResultMapper
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.data.remotedata.model.SearxResult
import mort.ar.searxme.network.SearchService
import org.junit.Before
import org.junit.Test

class SearchRemoteDataSourceImplTest {

    private lateinit var dataSource: SearchRemoteDataSourceImpl

    private lateinit var searchService: SearchService
    private val mapper = SearchResultMapper()

    @Before
    fun setUp() {
        TestSchedulerManager.throwEverythingOnTrampoline()

        searchService = mock()

        dataSource = SearchRemoteDataSourceImpl(searchService, mapper)
    }

    @Test
    fun `GIVEN call to api successful WHEN requestSearchResults() THEN list of SearchResults returned`() {
        whenever(
            searchService.requestSearchResults(
                query = searchRequest.query,
                categories = searchRequest.categories,
                engines = searchRequest.engines,
                language = searchRequest.language,
                pageNo = searchRequest.pageNo,
                timeRange = searchRequest.timeRange,
                format = searchRequest.format,
                imageProxy = searchRequest.imageProxy,
                autoComplete = searchRequest.autoComplete,
                safeSearch = searchRequest.safeSearch
            )
        ).thenReturn(Single.just(searchResponse))

        val testSingle = dataSource.requestSearchResults(searchRequest).test()

        testSingle
            .assertValue(listOf(searchResult))
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN call to api successful with empty results WHEN requestSearchResults() THEN empty list returned`() {
        val emptySearchResponse = searchResponse.copy(results = emptyList())
        whenever(
            searchService.requestSearchResults(
                query = searchRequest.query,
                categories = searchRequest.categories,
                engines = searchRequest.engines,
                language = searchRequest.language,
                pageNo = searchRequest.pageNo,
                timeRange = searchRequest.timeRange,
                format = searchRequest.format,
                imageProxy = searchRequest.imageProxy,
                autoComplete = searchRequest.autoComplete,
                safeSearch = searchRequest.safeSearch
            )
        ).thenReturn(Single.just(emptySearchResponse))

        val testSingle = dataSource.requestSearchResults(searchRequest).test()

        testSingle
            .assertValue(emptyList())
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN call to api fails WHEN requestSearchResults() THEN throws`() {
        val throwable = Throwable("error")
        whenever(
            searchService.requestSearchResults(
                query = searchRequest.query,
                categories = searchRequest.categories,
                engines = searchRequest.engines,
                language = searchRequest.language,
                pageNo = searchRequest.pageNo,
                timeRange = searchRequest.timeRange,
                format = searchRequest.format,
                imageProxy = searchRequest.imageProxy,
                autoComplete = searchRequest.autoComplete,
                safeSearch = searchRequest.safeSearch
            )
        ).thenReturn(Single.error(throwable))

        val testSingle = dataSource.requestSearchResults(searchRequest).test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

    @Test
    fun `GIVEN call to api succeeds WHEN requestSearchAutocomplete() THEN return a list of suggestions`() {
        val suggestions = listOf("one", "two")
        whenever(searchService.requestSearchAutocomplete(query, autoComplete)).thenReturn(Single.just(suggestions))

        val testSingle = dataSource.requestSearchAutocomplete(searchRequest).test()

        testSingle
            .assertValue(suggestions)
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN call to api fails WHEN requestSearchAutocomplete() THEN throws`() {
        val throwable = Throwable("error")
        whenever(searchService.requestSearchAutocomplete(query, autoComplete)).thenReturn(Single.error(throwable))

        val testSingle = dataSource.requestSearchAutocomplete(searchRequest).test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

    companion object {
        private const val title = "title"
        private const val url = "url"
        private const val prettyUrl = "pretty"
        private const val content = "content"
        private val engines = listOf("engine")
        private val searxResult = SearxResult(
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

        private val searchResult = SearchResult(
            title = title,
            url = url,
            prettyUrl = prettyUrl,
            content = content,
            engines = engines
        )

        private val searchResponse = SearchResponse(
            numberOfResults = 2,
            corrections = emptyList(),
            query = "",
            infoBoxes = emptyList(),
            suggestions = emptyList(),
            results = listOf(searxResult),
            answers = emptyList(),
            unresponsiveEngines = emptyList()
        )

        const val query = "query"
        const val autoComplete = "duckduckgo"
        private val searchRequest = SearchRequest(
            query = query,
            categories = "files,images",
            engines = "bing, duckduckgo",
            language = "de",
            pageNo = 1,
            timeRange = "DAY",
            format = "json",
            imageProxy = "",
            autoComplete = autoComplete,
            safeSearch = "true"
        )
    }

}