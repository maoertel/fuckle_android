package mort.ar.searxme.presentation.settings

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.domain.GetSettingsParameterUseCase
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.SettingsParameter
import mort.ar.searxme.presentation.model.TimeRanges
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
    private val settingsView: SettingsContract.SettingsView,
    private val getSettingsParameterUseCase: GetSettingsParameterUseCase,
    private val compositeDisposable: CompositeDisposable
) : SettingsContract.SettingsPresenter {

    override fun start() = settingsView.setSpinnerAdapters()

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

    override fun stop() = compositeDisposable.clear()

    override fun persistSettings(
        instance: String,
        language: Languages,
        timeRange: TimeRanges
    ) {
        TODO("Not implemented yet...")
    }

    override fun onEngineCheckBoxClick(engine: Engines, shouldAdd: Boolean) =
        when {
            shouldAdd -> settingsView.setEnginesDefaultCheckBoxActive(!shouldAdd)
            else -> /*if (engines.isEmpty())*/ settingsView.setEnginesDefaultCheckBoxActive(!shouldAdd)
        }

    override fun onCategoryCheckBoxClick(category: Categories, shouldAdd: Boolean) =
        when {
            shouldAdd -> settingsView.setCategoriesDefaultCheckBoxActive(!shouldAdd)
            else -> /*if (categories.isEmpty())*/ settingsView.setCategoriesDefaultCheckBoxActive(!shouldAdd)
        }

//    private fun assignSearchParameter(parameter: (String?) -> Unit) {
//        engines
//            .map { it.urlParameter }
//            .let { engineList ->
//                parameter(
//                    when {
//                        engineList.isNullOrEmpty() -> null
//                        else -> engineList.joinToString(separator = ",", prefix = "")
//                    }
//                )
//            }
//    }
//
//    private fun assignSearchParameterEngines() {
//        engines
//            .map { it.urlParameter }
//            .let { engineList ->
//                searchParameterTemp.engines =
//                    when {
//                        engineList.isNullOrEmpty() -> null
//                        else -> engineList.joinToString(separator = ",", prefix = "")
//                    }
//            }
//    }
//
//    private fun assignSearchParameterCategories() {
//        categories
//            .map { it.urlParameter }
//            .let { categoriesList ->
//                searchParameterTemp.categories =
//                    when {
//                        categoriesList.isNullOrEmpty() -> null
//                        else -> categoriesList.joinToString(separator = ",", prefix = "")
//                    }
//            }
//    }

}