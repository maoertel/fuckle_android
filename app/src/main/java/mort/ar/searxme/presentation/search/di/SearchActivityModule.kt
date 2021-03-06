package mort.ar.searxme.presentation.search.di

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.mapper.SearchRequestMapper
import mort.ar.searxme.di.ActivityScope
import mort.ar.searxme.domain.InsertInstanceUseCase
import mort.ar.searxme.domain.SearchRequestUseCase
import mort.ar.searxme.domain.SearchSuggestionsUseCase
import mort.ar.searxme.domain.usecases.InsertInstanceUseCaseImpl
import mort.ar.searxme.domain.usecases.SearchRequestUseCaseImpl
import mort.ar.searxme.domain.usecases.SearchSuggestionsUseCaseImpl
import mort.ar.searxme.presentation.search.*
import mort.ar.searxme.presentation.settings.SettingsActivity

@Module
class ActivitySearchModule {

    @ActivityScope
    @Provides
    fun provideSearchActivity(
        searchActivity: SearchActivity
    ): SearchContract.SearchView = searchActivity

    @ActivityScope
    @Provides
    fun provideSearchPresenter(
        searchView: SearchContract.SearchView,
        searchRequestUseCase: SearchRequestUseCase,
        searchSuggestionsUseCase: SearchSuggestionsUseCase,
        insertInstanceUseCase: InsertInstanceUseCase,
        compositeDisposable: CompositeDisposable
    ): SearchContract.SearchPresenter =
        SearchPresenter(
            searchView,
            searchRequestUseCase,
            searchSuggestionsUseCase,
            insertInstanceUseCase,
            compositeDisposable
        )

    @Provides
    fun provideSearchResultPresenter(searchPresenter: SearchContract.SearchPresenter) =
        searchPresenter as SearchContract.SearchResultPresenter

    @Provides
    fun provideSearchSuggestionsPresenter(searchPresenter: SearchContract.SearchPresenter) =
        searchPresenter as SearchContract.SearchSuggestionPresenter

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

    @Provides
    fun provideSettingsIntent(searchActivity: SearchActivity) =
        Intent(searchActivity, SettingsActivity::class.java)

    @Provides
    fun provideSearchRequestUseCase(
        searchResultRepository: SearchResultRepository,
        searchParameterRepository: SearchParameterRepository,
        searchRequestMapper: SearchRequestMapper
    ): SearchRequestUseCase =
        SearchRequestUseCaseImpl(searchResultRepository, searchParameterRepository, searchRequestMapper)

    @Provides
    fun provideSearchSuggestionsUseCase(
        searchResultRepository: SearchResultRepository,
        searchParameterRepository: SearchParameterRepository
    ): SearchSuggestionsUseCase = SearchSuggestionsUseCaseImpl(searchResultRepository, searchParameterRepository)

    @Provides
    fun provideInsertInstanceUseCase(
        searxInstanceRepository: SearxInstanceRepository
    ): InsertInstanceUseCase = InsertInstanceUseCaseImpl(searxInstanceRepository)

}