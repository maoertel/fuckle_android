package mort.ar.searxme.domain.usecases

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearchResultRepository
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
        val query = "hello"
        val autoComplete = "ddg"
        val suggestionsList = listOf("one", "two")
        whenever(searchParameterRepository.getAutoComplete()).thenReturn(Single.just(autoComplete))
        whenever(searchResultRepository.requestSearchAutoComplete(any()))
            .thenReturn(Single.just(suggestionsList))

        val testSingle = useCase.requestSearchAutoComplete(query).test()

        testSingle
            .assertValue(suggestionsList)
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN api call fails WHEN requestSearchAutoComplete() THEN throws`() {
        val query = "hello"
        val autoComplete = "ddg"
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
        val query = "hello"
        val throwable = Throwable("error")
        whenever(searchParameterRepository.getAutoComplete()).thenReturn(Single.error(throwable))

        val testSingle = useCase.requestSearchAutoComplete(query).test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

}