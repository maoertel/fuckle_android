package mort.ar.searxme.injection

import dagger.Module
import dagger.Provides
import mort.ar.searxme.manager.SearchManager
import mort.ar.searxme.search.SearchActivity
import mort.ar.searxme.search.SearchContract
import mort.ar.searxme.search.SearchPresenter


@Module
class SearchActivityModule {

    @Provides
    internal fun provideSearchActivity(
        searchActivity: SearchActivity
    ): SearchContract.SearchView = searchActivity

    @Provides
    internal fun provideSearchPresenter(
        mainView: SearchContract.SearchView,
        searchManager: SearchManager
    ): SearchPresenter = SearchPresenter(mainView, searchManager)

}
