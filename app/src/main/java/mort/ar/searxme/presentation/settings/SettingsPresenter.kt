package mort.ar.searxme.presentation.settings

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
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
            .subscribe(
                { initializeSettingsParameter(it) },
                { throwable -> Log.d(SettingsPresenter::class.simpleName, throwable.message) }
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
//        persistInstance(instance)
//        persistLanguage(language)
//        persistTimeRange(timeRange)
//        assignSearchParameterEngines()
//        assignSearchParameterCategories()
//        assignSearchParameter { run { searchParameterTemp.engines } }
//        assignSearchParameter { run { searchParameterTemp.categories } }
    }

    private fun persistInstance(searxInstance: String) {
//        compositeDisposable += searxInstanceRepositoryImpl.setPrimaryInstance(searxInstance)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { /* do nothing so far */ },
//                { throwable -> settingsView.showMessage(throwable.message) }
//            )
    }

    override fun onInstanceSpinnerItemSelected(selectedItem: String) {

    }

    private fun persistTimeRange(selectedTimeRange: TimeRanges) {
//        searchParameterTemp.timeRange = selectedTimeRange
    }

    private fun persistLanguage(selectedLanguage: Languages) {
//        searchParameterTemp.language = selectedLanguage
    }

    override fun onEngineCheckBoxClick(engine: Engines, shouldAdd: Boolean) {
//        when {
//            shouldAdd -> {
//                engines.add(engine)
//                settingsView.setEnginesDefaultCheckBoxActive(!shouldAdd)
//            }
//            else -> {
//                engines.remove(engine)
//                if (engines.isEmpty())
//                    settingsView.setEnginesDefaultCheckBoxActive(!shouldAdd)
//            }
//        }
    }

    override fun onCategoryCheckBoxClick(category: Categories, shouldAdd: Boolean) {
//        when {
//            shouldAdd -> {
//                categories.add(category)
//                settingsView.setCategoriesDefaultCheckBoxActive(!shouldAdd)
//            }
//            else -> {
//                categories.remove(category)
//                if (categories.isEmpty())
//                    settingsView.setCategoriesDefaultCheckBoxActive(!shouldAdd)
//            }
//        }
    }

//    private fun assignSearchParameter(parameter: (String?) -> Unit) {
//        val engineList = arrayListOf<String>()
//        this.engines.forEach { engineList.add(it.urlParameter) }
//        parameter(
//            when {
//                engineList.isNullOrEmpty() -> null
//                else -> engineList.joinToString(separator = ",", prefix = "")
//            }
//        )
//    }
//
//    private fun assignSearchParameterEngines() {
//        val engineList = arrayListOf<String>()
//        engines.forEach { engineList.add(it.urlParameter) }
//        searchParameterTemp.engines =
//            when {
//                engineList.isNullOrEmpty() -> null
//                else -> engineList.joinToString(separator = ",", prefix = "")
//            }
//    }
//
//    private fun assignSearchParameterCategories() {
//        val categoriesList = arrayListOf<String>()
//        categories.forEach { categoriesList.add(it.urlParameter) }
//        searchParameterTemp.categories =
//            when {
//                categoriesList.isNullOrEmpty() -> null
//                else -> categoriesList.joinToString(separator = ",", prefix = "")
//            }
//    }

}