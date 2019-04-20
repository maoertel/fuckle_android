package mort.ar.searxme

import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.data.repositories.SearchResultRepositoryImpl
import mort.ar.searxme.presentation.search.SearchContract
import org.mockito.Mock

class SearchUnitTest() {

    @Mock
    lateinit var searchView: SearchContract.SearchView

    @Mock
    lateinit var searchResultRepositoryImpl: SearchResultRepositoryImpl

    @Mock
    lateinit var compositeDisposable: CompositeDisposable


}