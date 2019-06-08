package mort.ar.searxme.data.repositories

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.remotedata.SearchRemoteDataSource
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.data.remotedata.model.SearxResult
import org.junit.Before
import org.junit.Test

class SearchResultRepositoryImplTest {

    private lateinit var repository: SearchResultRepositoryImpl

    private lateinit var searchRemoteDataSource: SearchRemoteDataSource

    @Before
    fun setUp() {
        TestSchedulerManager.throwEverythingOnTrampoline()

        searchRemoteDataSource = mock()

        repository = SearchResultRepositoryImpl(searchRemoteDataSource)
    }

    @Test
    fun `GIVEN call to api successful WHEN requestSearchResults() THEN search results returned`() {
        val searchResponse = mock<SearchResponse> { on { results } doReturn listOf() }
        whenever(searchRemoteDataSource.requestSearchResults(searchRequest)).thenReturn(Single.just(searchResponse))

        val testSingle = repository.requestSearchResults(searchRequest).test()

        testSingle
            .assertValue { it.results.isEmpty() }
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN call to api successful and results not empty WHEN requestSearchResults() THEN empty search results returned`() {
        val searchResponse = mock<SearchResponse> { on { results } doReturn listOf(searxResult) }
        whenever(searchRemoteDataSource.requestSearchResults(searchRequest)).thenReturn(Single.just(searchResponse))

        val testSingle = repository.requestSearchResults(searchRequest).test()

        testSingle
            .assertValue { it.results.isNotEmpty() && it.results.first() == searxResult }
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN call to api fails WHEN requestSearchResults() THEN throws`() {
        val throwable = Throwable("error")
        whenever(searchRemoteDataSource.requestSearchResults(searchRequest)).thenReturn(Single.error(throwable))

        val testSingle = repository.requestSearchResults(searchRequest).test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

    @Test
    fun `GIVEN call to api successful WHEN requestSearchAutoComplete() THEN list of suggestions returned`() {
        whenever(searchRemoteDataSource.requestSearchAutocomplete(searchRequest)).thenReturn(Single.just(emptyList()))

        val testSingle = repository.requestSearchAutoComplete(searchRequest).test()

        testSingle
            .assertValue(emptyList())
            .assertValue { it.isEmpty() }
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN call to api fails WHEN requestSearchAutoComplete() THEN throws`() {
        val throwable = Throwable("error")
        whenever(searchRemoteDataSource.requestSearchAutocomplete(searchRequest)).thenReturn(Single.error(throwable))

        val testSingle = repository.requestSearchAutoComplete(searchRequest).test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

    companion object {
        private val searchRequest = SearchRequest(query = "hello")
        private val searxResult = SearxResult(
            engine = "",
            category = "",
            engines = emptyList(),
            title = "",
            url = "",
            positions = emptyList(),
            parsed_url = emptyList(),
            content = "",
            prettyUrl = "",
            score = 0.0
        )
    }

}