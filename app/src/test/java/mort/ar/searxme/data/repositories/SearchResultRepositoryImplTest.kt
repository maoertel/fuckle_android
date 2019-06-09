package mort.ar.searxme.data.repositories

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.model.SearchResult
import mort.ar.searxme.data.remotedata.SearchRemoteDataSource
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
        val searchResult = mock<SearchResult>()
        whenever(searchRemoteDataSource.requestSearchResults(searchRequest)).thenReturn(Single.just(listOf(searchResult)))

        val testSingle = repository.requestSearchResults(searchRequest).test()

        testSingle
            .assertValue { it == listOf(searchResult) }
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN call to api successful and results not empty WHEN requestSearchResults() THEN empty search results returned`() {
        whenever(searchRemoteDataSource.requestSearchResults(searchRequest)).thenReturn(Single.just(emptyList()))

        val testSingle = repository.requestSearchResults(searchRequest).test()

        testSingle
            .assertValue { it.isEmpty() }
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