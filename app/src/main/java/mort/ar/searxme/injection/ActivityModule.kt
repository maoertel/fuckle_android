package mort.ar.searxme.injection

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import mort.ar.searxme.manager.Searcher
import mort.ar.searxme.search.*


@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [ActivitySearchModule::class])
    abstract fun contributeSearchActivity(): SearchActivity

}

@Module
internal class ActivitySearchModule {

    @Provides
    internal fun provideSearchActivity(
        searchActivity: SearchActivity
    ): SearchContract.SearchView = searchActivity

    @Provides
    internal fun provideSearchPresenter(
        mainView: SearchContract.SearchView,
        searcher: Searcher
    ): SearchPresenter = SearchPresenter(mainView, searcher)

    @Provides
    fun provideSearchContractPresenter(searchPresenter: SearchPresenter) =
        searchPresenter as SearchContract.SearchPresenter

    @Provides
    fun provideSearchResultPresenter(searchPresenter: SearchPresenter) =
        searchPresenter as SearchContract.SearchResultPresenter

    @Provides
    fun provideSearchSuggestionsPresenter(searchPresenter: SearchPresenter) =
        searchPresenter as SearchContract.SearchSuggestionPresenter

    @Provides
    fun provideSearchSuggestionsAdapter(searchPresenter: SearchPresenter) =
        SearchSuggestionsAdapter(searchPresenter)

    @Provides
    fun provideSearchResultAdapter(searchPresenter: SearchPresenter) =
        SearchResultAdapter(searchPresenter)

}
