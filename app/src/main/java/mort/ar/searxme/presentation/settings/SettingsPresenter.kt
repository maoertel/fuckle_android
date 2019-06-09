package mort.ar.searxme.presentation.settings

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.domain.GetSettingsParameterUseCase
import mort.ar.searxme.domain.SaveSettingsParameterUseCase
import mort.ar.searxme.data.model.SettingsParameter
import mort.ar.searxme.presentation.model.Categories
import mort.ar.searxme.presentation.model.Engines
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
    private val settingsView: SettingsContract.SettingsView,
    private val getSettingsParameterUseCase: GetSettingsParameterUseCase,
    private val saveSettingsParameterUseCase: SaveSettingsParameterUseCase,
    private val compositeDisposable: CompositeDisposable
) : SettingsContract.SettingsPresenter {

    override fun start() = settingsView.setSpinnerAdapters()

    override fun stop() = compositeDisposable.dispose()

    override fun loadSettings() {
        getSettingsParameterUseCase.getSettingsParameter()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { initializeSettingsParameter(it) },
                onError = { throwable -> Log.d(SettingsPresenter::class.simpleName, throwable.message) }
            )
            .addTo(compositeDisposable)
    }

    private fun initializeSettingsParameter(settingsParameter: SettingsParameter) {
        settingsView.initializeSearxInstanceSpinner(settingsParameter.searxInstances)
        settingsView.initializeLanguageSpinner(settingsParameter.language.ordinal)
        settingsView.initializeTimeRangeSpinner(settingsParameter.timeRange.ordinal)
        initializeEngines(settingsParameter.engines)
        initializeCategories(settingsParameter.categories)
    }

    private fun initializeEngines(engines: List<Engines>) {
        settingsView.initializeEnginesDefaultCheckbox(engines.isEmpty())
        Engines.values().forEach { engine ->
            settingsView.initializeEngineCheckBox(engine, engines.contains(engine))
        }
    }

    private fun initializeCategories(categories: List<Categories>) {
        settingsView.initializeCategoriesDefaultCheckbox(categories.isEmpty())
        Categories.values().forEach { category ->
            settingsView.initializeCategoryCheckBox(category, categories.contains(category))
        }
    }

    override fun onEnginesDefaultCheckboxClick() {
        settingsView.setEnginesDefaultCheckBoxActive(true)
        Engines.values().forEach { engine ->
            settingsView.setCheckBoxActive(engine.checkBox, false)
        }
    }

    override fun onCategoriesDefaultCheckboxClick() {
        settingsView.setCategoriesDefaultCheckBoxActive(true)
        Categories.values().forEach { category ->
            settingsView.setCheckBoxActive(category.checkBox, false)
        }
    }

    override fun persistSettings(settingsParameter: SettingsParameter, onFinished: () -> Unit) {
        saveSettingsParameterUseCase
            .saveSettingsParameter(settingsParameter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    onFinished()
                    settingsView.showMessage("Settings successfully saved")
                },
                onError = {
                    onFinished()
                    settingsView.showMessage("Something went wrong")
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onEngineCheckBoxClick(shouldCheck: Boolean, enginesEmpty: Boolean) =
        when {
            shouldCheck -> settingsView.setEnginesDefaultCheckBoxActive(false)
            enginesEmpty -> settingsView.setEnginesDefaultCheckBoxActive(true)
            else -> Unit
        }

    override fun onCategoryCheckBoxClick(shouldCheck: Boolean, categoriesEmpty: Boolean) =
        when {
            shouldCheck -> settingsView.setCategoriesDefaultCheckBoxActive(false)
            categoriesEmpty -> settingsView.setCategoriesDefaultCheckBoxActive(true)
            else -> Unit
        }

}