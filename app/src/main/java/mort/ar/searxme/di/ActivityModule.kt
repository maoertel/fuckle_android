package mort.ar.searxme.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mort.ar.searxme.presentation.search.SearchActivity
import mort.ar.searxme.presentation.search.di.ActivitySearchModule
import mort.ar.searxme.presentation.settings.SettingsActivity
import mort.ar.searxme.presentation.settings.di.ActivitySettingsModule
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