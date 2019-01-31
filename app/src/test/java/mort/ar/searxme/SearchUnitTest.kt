package mort.ar.searxme

import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.manager.Searcher
import mort.ar.searxme.search.SearchContract
import org.mockito.Mock

class SearchUnitTest() {

    @Mock
    lateinit var searchView: SearchContract.SearchView

    @Mock
    lateinit var searcher: Searcher

    @Mock
    lateinit var compositeDisposable: CompositeDisposable


}