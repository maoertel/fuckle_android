package mort.ar.searxme.presentation.settings.di

import android.R
import android.widget.ArrayAdapter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.di.ActivityScope
import mort.ar.searxme.domain.GetSettingsParameterUseCase
import mort.ar.searxme.domain.SaveSettingsParameterUseCase
import mort.ar.searxme.data.mapper.SettingsParameterMapper
import mort.ar.searxme.domain.usecases.GetSettingsParameterUseCaseImpl
import mort.ar.searxme.domain.usecases.SaveSettingsParameterUseCaseImpl
import mort.ar.searxme.presentation.model.Categories
import mort.ar.searxme.presentation.model.Engines
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges
import mort.ar.searxme.presentation.settings.*

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
        getSettingsParameterUseCase: GetSettingsParameterUseCase,
        saveSettingsParameterUseCase: SaveSettingsParameterUseCase,
        compositeDisposable: CompositeDisposable
    ): SettingsContract.SettingsPresenter =
        SettingsPresenter(
            settingsView,
            getSettingsParameterUseCase,
            saveSettingsParameterUseCase,
            compositeDisposable
        )

    @Provides
    fun provideEnginesHashSet() = hashSetOf<Engines>()

    @Provides
    fun provideCategoriesHashSet() = hashSetOf<Categories>()

    @Provides
    fun provideTimeRangeAdapter(settingsActivity: SettingsActivity) =
        ArrayAdapter(
            settingsActivity,
            R.layout.simple_spinner_dropdown_item,
            TimeRanges.values()
        )

    @Provides
    fun provideLanguageAdapter(settingsActivity: SettingsActivity) =
        ArrayAdapter(
            settingsActivity,
            R.layout.simple_spinner_dropdown_item,
            Languages.values()
        )

    @Provides
    fun provideSearxInstanceAdapter(settingsActivity: SettingsActivity) =
        ArrayAdapter(
            settingsActivity,
            R.layout.simple_spinner_dropdown_item,
            arrayListOf<String>()
        )

    @Provides
    fun provideGetSettingsParameterUseCase(
        searxInstanceRepository: SearxInstanceRepository,
        searchParameterRepository: SearchParameterRepository,
        settingsParameterMapper: SettingsParameterMapper
    ): GetSettingsParameterUseCase =
        GetSettingsParameterUseCaseImpl(searxInstanceRepository, searchParameterRepository, settingsParameterMapper)

    @Provides
    fun provideSaveSettingsParameterUseCase(
        searxInstanceRepository: SearxInstanceRepository,
        searchParameterRepository: SearchParameterRepository
    ): SaveSettingsParameterUseCase =
        SaveSettingsParameterUseCaseImpl(searxInstanceRepository, searchParameterRepository)

}