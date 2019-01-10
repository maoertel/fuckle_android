package mort.ar.searxme.injection

import android.content.Intent
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.WebViewFragment
import mort.ar.searxme.manager.SearchParameter
import mort.ar.searxme.manager.Searcher
import mort.ar.searxme.manager.SearxInstanceBucket
import mort.ar.searxme.model.Languages
import mort.ar.searxme.model.TimeRanges
import mort.ar.searxme.search.*
import mort.ar.searxme.settings.*
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope


@Module
internal abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivitySearchModule::class])
    abstract fun contributeSearchActivity(): SearchActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivitySettingsModule::class])
    abstract fun contributeSettingsActivity(): SettingsActivity

}

@Module
internal class ActivitySettingsModule {

    @ActivityScope
    @Provides
    fun provideSettingsActivity(
        settingsActivity: SettingsActivity
    ): SettingsContract.SettingsView = settingsActivity

    @ActivityScope
    @Provides
    fun provideSettingsPresenter(
        settingsView: SettingsContract.SettingsView,
        searchParameter: SearchParameter,
        searxInstanceBucket: SearxInstanceBucket,
        engines: HashSet<Engines>,
        categories: HashSet<Categories>,
        compositeDisposable: CompositeDisposable
    ): SettingsContract.SettingsPresenter =
        SettingsPresenter(
            settingsView,
            searchParameter,
            searxInstanceBucket,
            engines,
            categories,
            compositeDisposable
        )

    @Provides
    fun provideEnginesHashSet() =
        hashSetOf<Engines>()

    @Provides
    fun provideCategoriesHashSet() =
        hashSetOf<Categories>()

    @Provides
    fun provideTimeRangeAdapter(settingsActivity: SettingsActivity) =
        ArrayAdapter(
            settingsActivity,
            android.R.layout.simple_spinner_dropdown_item,
            TimeRanges.values()
        )

    @Provides
    fun provideLanguageAdapter(settingsActivity: SettingsActivity) =
        ArrayAdapter(
            settingsActivity,
            android.R.layout.simple_spinner_dropdown_item,
            Languages.values()
        )

    @Provides
    fun provideSearxInstanceAdapter(settingsActivity: SettingsActivity) =
        ArrayAdapter(
            settingsActivity,
            android.R.layout.simple_spinner_dropdown_item,
            arrayListOf<String>()
        )

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
        searcher: Searcher,
        compositeDisposable: CompositeDisposable
    ): SearchContract.SearchPresenter = SearchPresenter(searchView, searcher, compositeDisposable)

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

}
