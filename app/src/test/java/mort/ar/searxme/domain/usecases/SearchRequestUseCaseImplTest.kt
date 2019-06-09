package mort.ar.searxme.domain.usecases

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.mapper.SearchRequestMapper
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.model.SearchResult
import mort.ar.searxme.presentation.model.Languages.ENGLISH
import mort.ar.searxme.presentation.model.TimeRanges.DAY
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchRequestUseCaseImplTest {

    private lateinit var useCase: SearchRequestUseCaseImpl

    private lateinit var searchResultRepository: SearchResultRepository
    private lateinit var searchParameterRepository: SearchParameterRepository
    private val mapper = SearchRequestMapper()

    @Before
    fun setUp() {
        TestSchedulerManager.throwEverythingOnTrampoline()

        searchResultRepository = mock()
        searchParameterRepository = mock {
            on { getCategories() } doReturn Single.just(categories)
            on { getEngines() } doReturn Single.just(engines)
            on { getLanguage() } doReturn Single.just(ENGLISH)
            on { getPageNo() } doReturn Single.just(pageNo)
            on { getTimeRange() } doReturn Single.just(DAY)
            on { getFormat() } doReturn Single.just(format)
            on { getImageProxy() } doReturn Single.just(imageProxy)
            on { getAutoComplete() } doReturn Single.just(autoComplete)
            on { getSafeSearch() } doReturn Single.just(safeSearch)
        }

        useCase = SearchRequestUseCaseImpl(searchResultRepository, searchParameterRepository, mapper)
    }

    @Test
    fun `GIVEN call succeeds WHEN requestSearchResults() called THEN list of search results`() {
        val captor = argumentCaptor<SearchRequest>()
        whenever(searchResultRepository.requestSearchResults(captor.capture()))
            .thenReturn(Single.just(listOf(searchResult)))

        val testSingle = useCase.requestSearchResults(query).test()

        assertEquals(expectedSearchRequest, captor.firstValue)
        testSingle
            .assertValue(listOf(searchResult))
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN call to api fails WHEN requestSearchResults() called THEN throws`() {
        val throwable = Throwable("error")
        val captor = argumentCaptor<SearchRequest>()
        whenever(searchResultRepository.requestSearchResults(captor.capture()))
            .thenReturn(Single.error(throwable))

        val testSingle = useCase.requestSearchResults(query).test()

        assertEquals(expectedSearchRequest, captor.firstValue)
        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

    companion object {
        private const val query = "hello"
        private const val categories = "files,images"
        private const val engines = "bing,duckduckgo"
        private const val language = "en"
        private const val pageNo = 1
        private const val timeRange = "day"
        private const val format = "hello"
        private const val imageProxy = "42"
        private const val autoComplete = "duckduckgo"
        private const val safeSearch = "23"

        private val searchResult = SearchResult(
            title = "title",
            url = "http://www.nose.com",
            prettyUrl = "pretty",
            content = "11!!1!",
            engines = listOf("bing", "duckduckgo")
        )

        private val expectedSearchRequest = SearchRequest(
            query = query,
            categories = categories,
            engines = engines,
            language = language,
            pageNo = pageNo,
            timeRange = timeRange,
            format = format,
            imageProxy = imageProxy,
            autoComplete = autoComplete,
            safeSearch = safeSearch
        )
    }

}