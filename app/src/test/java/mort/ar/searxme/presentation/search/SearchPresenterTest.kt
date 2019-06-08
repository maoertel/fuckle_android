package mort.ar.searxme.presentation.search

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.domain.SearchRequestUseCase
import mort.ar.searxme.domain.SearchSuggestionsUseCase
import mort.ar.searxme.presentation.model.SearchResults
import mort.ar.searxme.presentation.search.Pages.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible

class SearchPresenterTest {

    private lateinit var presenter: SearchContract.SearchPresenter

    private lateinit var searchView: SearchContract.SearchView
    private lateinit var searchRequestUseCase: SearchRequestUseCase
    private lateinit var searchSuggestionsUseCase: SearchSuggestionsUseCase
    private lateinit var compositeDisposable: CompositeDisposable


    @Before
    fun setUp() {
        TestSchedulerManager.throwEverythingOnTrampoline()

        searchView = mock()
        searchRequestUseCase = mock()
        searchSuggestionsUseCase = mock()
        compositeDisposable = mock()

        presenter = SearchPresenter(searchView, searchRequestUseCase, searchSuggestionsUseCase, compositeDisposable)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> SearchContract.SearchPresenter.getValueFromPrivateProperty(propertyName: String): T? =
        this::class.members.findLast { it.name == propertyName }?.let { field ->
            field.isAccessible = true
            (field as? KProperty1<SearchContract.SearchPresenter, T>)?.get(this)
        }

    @Suppress("UNCHECKED_CAST")
    private fun <T> SearchContract.SearchPresenter.setValueOfPrivateMutableProperty(propertyName: String, value: T) =
        this::class.members.findLast { it.name == propertyName }?.let { field ->
            field.isAccessible = true
            (field as? KMutableProperty1<SearchContract.SearchPresenter, T>)?.set(this, value)
        }

    @Test
    fun `GIVEN WHEN THEN`() {

    }

    @Test
    fun `GIVEN nothing WHEN start() called THEN WebView and adapters have been initialized`() {
        presenter.start()

        verify(searchView).initializeWebViewFragment()
        verify(searchView).initializeSearchSuggestionsAdapter()
        verify(searchView).initializeSearchResultsAdapter()
    }

    @Test
    fun `GIVEN nothing WHEN stop() called THEN compositeDisposables should be disposed`() {
        presenter.stop()

        verify(compositeDisposable).dispose()
    }

    @Test
    fun `GIVEN query WHEN onSearchSuggestionClick() THEN query fired, search box updated with query text, suggestionList to true`() {
        val query = "Hallo"
        val suggestionsList = mock<SearchResults>()
        whenever(searchRequestUseCase.requestSearchResults(query)).thenReturn(Single.just(suggestionsList))

        presenter.onSearchSuggestionClick(query)

        verify(searchView).setSearchQuery(query)
        verify(searchView).hideKeyboard()
        verify(searchView).hideSearchSuggestions()
        verify(searchView).hideWebView()
        verify(searchView).showSearchResults()
        verify(searchView).showProgress()
        verify(searchView).hideProgress()
        verify(searchView).updateSearchResults(suggestionsList)
        assertTrue(presenter.getValueFromPrivateProperty<Boolean>("isSuggestionListSubmit") ?: false)
        assertEquals(SEARCH_RESULTS, presenter.getValueFromPrivateProperty<Pages>("currentPage"))
    }

    @Test
    fun `GIVEN nothing WHEN onHomeButtonClick() called THEN startPage shown`() {
        val testValue = presenter.onHomeButtonClick()

        verify(searchView).setSearchQuery("")
        verify(searchView).hideKeyboard()
        verify(searchView).hideSearchSuggestions()
        verify(searchView).hideSearchResults()
        verify(searchView).hideWebView()
        assertFalse(testValue)
        assertFalse(presenter.getValueFromPrivateProperty<Boolean>("isSuggestionListSubmit") ?: true)
        assertEquals(START, presenter.getValueFromPrivateProperty<Pages>("currentPage"))
    }

    @Test
    fun `GIVEN nothing WHEN onSettingsButtonClick() called THEN settingsPage shown`() {
        val testValue = presenter.onSettingsButtonClick()

        assertFalse(testValue)
        verify(searchView).startSettings()
    }

    @Test
    fun `GIVEN from suggestions click, not empty WHEN onQueryTextSubmit() THEN suggestions indicator will be updated`() {
        val query = "Hallo"
        presenter.setValueOfPrivateMutableProperty("isSuggestionListSubmit", true)

        val testValue = presenter.onQueryTextChange(query)

        assertTrue(testValue)
        assertEquals(false, presenter.getValueFromPrivateProperty("isSuggestionListSubmit"))
    }

    @Test
    fun `GIVEN not from suggestions click, not empty WHEN onQueryTextSubmit() THEN searchView will be updated`() {
        val query = "Hallo"
        val suggestions = listOf("one", "two")
        whenever(searchSuggestionsUseCase.requestSearchAutoComplete(query)).thenReturn(Single.just(suggestions))

        val testValue = presenter.onQueryTextChange(query)

        verify(searchView).updateSearchSuggestions(suggestions)
        assertTrue(testValue)
        assertEquals(false, presenter.getValueFromPrivateProperty("isSuggestionListSubmit"))
    }

    @Test
    fun `GIVEN not from suggestions click, not empty, request fails WHEN onQueryTextSubmit() THEN message shown to the user`() {
        val query = "Hallo"
        val errorMessage = "error"
        whenever(searchSuggestionsUseCase.requestSearchAutoComplete(query))
            .thenReturn(Single.error(Throwable(errorMessage)))

        val testValue = presenter.onQueryTextChange(query)

        verify(searchView).showMessage(errorMessage)
        assertTrue(testValue)
        assertEquals(false, presenter.getValueFromPrivateProperty("isSuggestionListSubmit"))
    }

    @Test
    fun `GIVEN not from suggestions click, query empty WHEN onQueryTextSubmit() THEN hide search suggestions`() {
        val query = ""

        val testValue = presenter.onQueryTextChange(query)

        verify(searchView).hideSearchSuggestions()
        assertTrue(testValue)
        assertEquals(false, presenter.getValueFromPrivateProperty("isSuggestionListSubmit"))
    }

    @Test
    fun `GIVEN current page is search results page WHEN handleOnBackPress() THEN show start page and true returned`() {
        presenter.setValueOfPrivateMutableProperty("currentPage", SEARCH_RESULTS)

        val testValue = presenter.handleOnBackPress()

        verify(searchView).setSearchQuery("")
        verify(searchView).hideKeyboard()
        verify(searchView).hideSearchSuggestions()
        verify(searchView).hideSearchResults()
        verify(searchView).hideWebView()
        assertFalse(presenter.getValueFromPrivateProperty<Boolean>("isSuggestionListSubmit") ?: true)
        assertEquals(START, presenter.getValueFromPrivateProperty<Pages>("currentPage"))
        assertTrue(testValue)
    }

    @Test
    fun `GIVEN current page is WebView and on back pressed is handled by WebView WHEN handleOnBackPress() THEN true returned`() {
        presenter.setValueOfPrivateMutableProperty("currentPage", WEBVIEW)
        whenever(searchView.onBackPressedHandledByWebView()).thenReturn(true)

        val testValue = presenter.handleOnBackPress()

        assertEquals(WEBVIEW, presenter.getValueFromPrivateProperty<Pages>("currentPage"))
        assertTrue(testValue)
    }

    @Test
    fun `GIVEN current page is WebView and on back pressed is not handled by WebView WHEN handleOnBackPress() THEN true returned, page search results`() {
        presenter.setValueOfPrivateMutableProperty("currentPage", WEBVIEW)
        whenever(searchView.onBackPressedHandledByWebView()).thenReturn(false)

        val testValue = presenter.handleOnBackPress()

        verify(searchView).hideKeyboard()
        verify(searchView).hideSearchSuggestions()
        verify(searchView).hideWebView()
        verify(searchView).showSearchResults()
        assertEquals(SEARCH_RESULTS, presenter.getValueFromPrivateProperty<Pages>("currentPage"))
        assertTrue(testValue)
    }

    @Test
    fun `GIVEN current page is start page and last click before quit WHEN handleOnBackPress() THEN false returned`() {
        presenter.setValueOfPrivateMutableProperty("currentPage", START)
        presenter.setValueOfPrivateMutableProperty("isLastClickBeforeQuitApp", true)

        val testValue = presenter.handleOnBackPress()

        assertEquals(START, presenter.getValueFromPrivateProperty<Pages>("currentPage"))
        assertFalse(testValue)
    }

    @Test
    fun `GIVEN current page is start page and not the last click before quit WHEN handleOnBackPress() THEN true returned, message shown`() {
        presenter.setValueOfPrivateMutableProperty("currentPage", START)
        presenter.setValueOfPrivateMutableProperty("isLastClickBeforeQuitApp", false)

        val testValue = presenter.handleOnBackPress()

        verify(searchView).showMessage(anyOrNull())
        assertEquals(START, presenter.getValueFromPrivateProperty<Pages>("currentPage"))
        assertTrue(presenter.getValueFromPrivateProperty<Boolean>("isLastClickBeforeQuitApp") ?: false)
        assertTrue(testValue)
    }

}