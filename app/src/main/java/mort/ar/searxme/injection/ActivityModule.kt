package mort.ar.searxme.injection

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import mort.ar.searxme.WebViewFragment
import mort.ar.searxme.manager.Searcher
import mort.ar.searxme.search.*
import mort.ar.searxme.settings.SettingsActivity
import mort.ar.searxme.settings.SettingsContract
import mort.ar.searxme.settings.SettingsPresenter
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
        settingsView: SettingsContract.SettingsView
    ): SettingsContract.SettingsPresenter = SettingsPresenter(settingsView)

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
