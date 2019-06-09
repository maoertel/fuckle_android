package mort.ar.searxme.domain.usecases

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.model.SearchRequest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchSuggestionsUseCaseTest {

    private lateinit var useCase: SearchSuggestionsUseCaseImpl

    private lateinit var searchResultRepository: SearchResultRepository
    private lateinit var searchParameterRepository: SearchParameterRepository

    @Before
    fun setUp() {
        TestSchedulerManager.throwEverythingOnTrampoline()

        searchResultRepository = mock()
        searchParameterRepository = mock()

        useCase = SearchSuggestionsUseCaseImpl(searchResultRepository, searchParameterRepository)
    }

    @Test
    fun `GIVEN api call and repo call successful WHEN requestSearchAutoComplete() THEN list of suggestions`() {
        val suggestionsList = listOf("one", "two")
        val captor = argumentCaptor<SearchRequest>()
        whenever(searchParameterRepository.getAutoComplete()).thenReturn(Single.just(autoComplete))
        whenever(searchResultRepository.requestSearchAutoComplete(captor.capture()))
            .thenReturn(Single.just(suggestionsList))

        val testSingle = useCase.requestSearchAutoComplete(query).test()

        assertEquals(expectedSearchRequest, captor.firstValue)
        testSingle
            .assertValue(suggestionsList)
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN api call fails WHEN requestSearchAutoComplete() THEN throws`() {
        val throwable = Throwable("error")
        whenever(searchParameterRepository.getAutoComplete()).thenReturn(Single.just(autoComplete))
        whenever(searchResultRepository.requestSearchAutoComplete(any()))
            .thenReturn(Single.error(throwable))

        val testSingle = useCase.requestSearchAutoComplete(query).test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

    @Test
    fun `GIVEN repo query fails WHEN requestSearchAutoComplete() THEN throws`() {
        val throwable = Throwable("error")
        whenever(searchParameterRepository.getAutoComplete()).thenReturn(Single.error(throwable))

        val testSingle = useCase.requestSearchAutoComplete(query).test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

    companion object {
        private const val query = "hello"
        private const val autoComplete = "duckduckgo"
        private val expectedSearchRequest = SearchRequest(
            query = query,
            autoComplete = autoComplete
        )
    }

}