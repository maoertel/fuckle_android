package mort.ar.searxme.presentation.settings

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.domain.GetSettingsParameterUseCase
import mort.ar.searxme.domain.SaveSettingsParameterUseCase
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.data.model.SettingsParameter
import mort.ar.searxme.presentation.model.TimeRanges
import org.junit.Before
import org.junit.Test

class SettingsPresenterTest {

    private lateinit var presenter: SettingsContract.SettingsPresenter

    private lateinit var settingsView: SettingsContract.SettingsView
    private lateinit var getSettingsParameterUseCase: GetSettingsParameterUseCase
    private lateinit var saveSettingsParameterUseCase: SaveSettingsParameterUseCase
    private lateinit var compositeDisposable: CompositeDisposable

    @Before
    fun setUp() {
        TestSchedulerManager.throwEverythingOnTrampoline()

        settingsView = mock()
        getSettingsParameterUseCase = mock()
        saveSettingsParameterUseCase = mock()
        compositeDisposable = mock()

        presenter = SettingsPresenter(
            settingsView,
            getSettingsParameterUseCase,
            saveSettingsParameterUseCase,
            compositeDisposable
        )
    }

    @Test
    fun `GIVEN nothing WHEN start() called THEN spinner adapters set up`() {
        presenter.start()

        verify(settingsView).setSpinnerAdapters()
    }

    @Test
    fun `GIVEN nothing WHEN stop() called THEN composite disposable disposed`() {
        presenter.stop()

        verify(compositeDisposable).dispose()
    }

    @Test
    fun `GIVEN api call succeeds WHEN loadSettings() called THEN all settings will be initialized`() {
        whenever(getSettingsParameterUseCase.getSettingsParameter()).thenReturn(Single.just(settingsParameter))

        presenter.loadSettings()

        verify(settingsView).initializeSearxInstanceSpinner(settingsParameter.searxInstances)
        verify(settingsView).initializeLanguageSpinner(settingsParameter.language.ordinal)
        verify(settingsView).initializeTimeRangeSpinner(settingsParameter.timeRange.ordinal)
        verify(settingsView).initializeEnginesDefaultCheckbox(settingsParameter.engines.isEmpty())
        verify(settingsView, times(Engines.values().size)).initializeEngineCheckBox(any(), any())
        verify(settingsView, times(Categories.values().size)).initializeCategoryCheckBox(any(), any())
    }

    @Test
    fun `GIVEN api call fails WHEN loadSettings() called THEN throws`() {
        whenever(getSettingsParameterUseCase.getSettingsParameter()).thenReturn(Single.error(Throwable("error")))

        presenter.loadSettings()

        verify(settingsView, never()).initializeSearxInstanceSpinner(settingsParameter.searxInstances)
        verify(settingsView, never()).initializeLanguageSpinner(settingsParameter.language.ordinal)
        verify(settingsView, never()).initializeTimeRangeSpinner(settingsParameter.timeRange.ordinal)
        verify(settingsView, never()).initializeEnginesDefaultCheckbox(settingsParameter.engines.isEmpty())
        verify(settingsView, never()).initializeEngineCheckBox(any(), any())
        verify(settingsView, never()).initializeCategoryCheckBox(any(), any())
    }

    @Test
    fun `GIVEN nothing WHEN onEnginesDefaultCheckboxClick() THEN engine checkboxes initialized`() {
        presenter.onEnginesDefaultCheckboxClick()

        verify(settingsView).setEnginesDefaultCheckBoxActive(true)
        Engines.values().forEach { engine ->
            verify(settingsView).setCheckBoxActive(engine.checkBox, false)
        }
    }

    @Test
    fun `GIVEN nothing WHEN onCategoriesDefaultCheckboxClick() THEN category checkboxes initialized`() {
        presenter.onCategoriesDefaultCheckboxClick()

        verify(settingsView).setCategoriesDefaultCheckBoxActive(true)
        Categories.values().forEach { category ->
            verify(settingsView).setCheckBoxActive(category.checkBox, false)
        }
    }

    @Test
    fun `GIVEN settingsParameter and successful query WHEN persistSettings() called THEN callBack is called and success message shown to the user`() {
        whenever(saveSettingsParameterUseCase.saveSettingsParameter(settingsParameter)).thenReturn(Completable.complete())
        val onFinished: () -> Unit = mock()

        presenter.persistSettings(settingsParameter, onFinished)

        verify(onFinished).invoke()
        verify(settingsView).showMessage(any())
    }

    @Test
    fun `GIVEN settingsParameter and failing query WHEN persistSettings() called THEN callBack is called and error message shown to the user`() {
        val onFinished: () -> Unit = mock()
        whenever(saveSettingsParameterUseCase.saveSettingsParameter(settingsParameter))
            .thenReturn(Completable.error(Throwable("error")))

        presenter.persistSettings(settingsParameter, onFinished)

        verify(onFinished).invoke()
        verify(settingsView).showMessage(any())
    }

    @Test
    fun `GIVEN shouldCheck true WHEN onEngineCheckBoxClick() THEN default engines checkbox unchecked`() {
        val shouldAdd = true

        presenter.onEngineCheckBoxClick(shouldAdd, true)

        verify(settingsView).setEnginesDefaultCheckBoxActive(false)
    }

    @Test
    fun `GIVEN shouldCheck false and engines empty WHEN onEngineCheckBoxClick() THEN default engines checkbox checked`() {
        val shouldAdd = false
        val enginesEmpty = true

        presenter.onEngineCheckBoxClick(shouldAdd, enginesEmpty)

        verify(settingsView).setEnginesDefaultCheckBoxActive(true)
    }

    @Test
    fun `GIVEN shouldCheck false and engines not empty WHEN onEngineCheckBoxClick() THEN nothing happens`() {
        val shouldAdd = false
        val enginesEmpty = false

        presenter.onEngineCheckBoxClick(shouldAdd, enginesEmpty)

        verify(settingsView, never()).setEnginesDefaultCheckBoxActive(any())
    }

    @Test
    fun `GIVEN shouldCheck true WHEN onCategoryCheckBoxClick() THEN default categories checkbox unchecked`() {
        val shouldAdd = true

        presenter.onCategoryCheckBoxClick(shouldAdd, true)

        verify(settingsView).setCategoriesDefaultCheckBoxActive(false)
    }

    @Test
    fun `GIVEN shouldCheck false and categories empty WHEN onCategoryCheckBoxClick() THEN default categories checkbox checked`() {
        val shouldAdd = false
        val categoriesEmpty = true

        presenter.onCategoryCheckBoxClick(shouldAdd, categoriesEmpty)

        verify(settingsView).setCategoriesDefaultCheckBoxActive(true)
    }

    @Test
    fun `GIVEN shouldCheck false and categories not empty WHEN onCategoryCheckBoxClick() THEN nothing happens`() {
        val shouldAdd = false
        val categoriesEmpty = false

        presenter.onCategoryCheckBoxClick(shouldAdd, categoriesEmpty)

        verify(settingsView, never()).setCategoriesDefaultCheckBoxActive(any())
    }

    companion object {
        val settingsParameter = SettingsParameter(
            searxInstances = mock(),
            engines = emptyList(),
            categories = emptyList(),
            language = Languages.GERMAN,
            timeRange = TimeRanges.DAY
        )
    }

}