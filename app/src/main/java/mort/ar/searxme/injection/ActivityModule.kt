package mort.ar.searxme.injection

import android.support.v7.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import mort.ar.searxme.WebViewFragment
import mort.ar.searxme.manager.Searcher
import mort.ar.searxme.search.*
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope


@Module
internal abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivitySearchModule::class])
    abstract fun contributeSearchActivity(): SearchActivity

}

@Module
internal class ActivitySearchModule {

    @ActivityScope
    @Provides
    fun provideSearchActivity(
        searchActivity: SearchActivity
    ): SearchContract.SearchView = searchActivity

    @ActivityScope
    @Provides
    fun provideSearchPresenter(
        searchView: SearchContract.SearchView,
        searcher: Searcher
    ): SearchContract.SearchPresenter = SearchPresenter(searchView, searcher)

    /*   @Provides
    fun provideSearchContractPresenter(searchPresenter: SearchPresenter) =
        searchPresenter as SearchContract.SearchPresenter

    @Provides
    fun provideSearchResultPresenter(searchPresenter: SearchPresenter) =
        searchPresenter as SearchContract.SearchResultPresenter

    @Provides
    fun provideSearchSuggestionsPresenter(searchPresenter: SearchPresenter) =
        searchPresenter as SearchContract.SearchSuggestionPresenter*/

    @Provides
    fun provideSearchSuggestionsAdapter(searchPresenter: SearchContract.SearchPresenter) =
        SearchSuggestionsAdapter(searchPresenter)

    @Provides
    fun provideSearchResultAdapter(searchPresenter: SearchContract.SearchPresenter) =
        SearchResultAdapter(searchPresenter)

    @Provides
    fun provideWebViewFragment() =
        WebViewFragment()

    @Provides
    fun provideLinearLayoutManager(searchActivity: SearchActivity) =
        LinearLayoutManager(searchActivity)

}
