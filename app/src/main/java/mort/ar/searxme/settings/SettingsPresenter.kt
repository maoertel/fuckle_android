package mort.ar.searxme.settings

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.manager.SearchParameter
import mort.ar.searxme.manager.SearxInstanceBucket
import mort.ar.searxme.model.Languages
import mort.ar.searxme.model.TimeRanges
import mort.ar.searxme.settings.SettingsActivity.Categories
import mort.ar.searxme.settings.SettingsActivity.Engines
import javax.inject.Inject


class SettingsPresenter @Inject constructor(
    private val settingsView: SettingsContract.SettingsView,
    private val searchParameter: SearchParameter,
    private val searxInstanceBucket: SearxInstanceBucket,
    private val engines: HashSet<SettingsActivity.Engines>,
    private val categories: HashSet<SettingsActivity.Categories>,
    private val compositeDisposable: CompositeDisposable
) : SettingsContract.SettingsPresenter {

    override fun start() {
    }

    override fun loadSettings() {
        initializeSearxInstanceSpinner()
        settingsView.initializeTimeRangeSpinner(searchParameter.searchParams.timeRange.ordinal)
        settingsView.initializeLanguageSpinner(searchParameter.searchParams.language.ordinal)
        initializeEngines()
        initializeCategories()
    }

    override fun persistSettings() {
        assignSearchParameterEngines()
        assignSearchParameterCategories()
    }

    override fun stop() {
        compositeDisposable.clear()
    }

    private fun initializeSearxInstanceSpinner() {
        settingsView.showProgress()
        compositeDisposable += searxInstanceBucket.getAllInstances()
            .flatMap { spinnerSearxInstances ->
                val instances = arrayListOf<String>()
                spinnerSearxInstances.forEach { instances.add(it.url) }
                Observable.just(instances)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { instances ->
                    settingsView.initializeSearxInstanceSpinner(instances, 0)
                    settingsView.hideProgress()
                },
                { throwable ->
                    settingsView.showMessage(throwable.message)
                    settingsView.hideProgress()
                }
            )
    }

    override fun onSearxInstanceSelect(searxInstance: String) {
        searxInstanceBucket.setCurrentInstance(searxInstance)
    }

    override fun onTimeRangeSelect(selectedTimeRange: TimeRanges) {
        searchParameter.searchParams.timeRange = selectedTimeRange
    }

    override fun onLanguageSelect(selectedLanguage: Languages) {
        searchParameter.searchParams.language = selectedLanguage
    }

    private fun initializeEngines() {
        initializeEnginesSet()
        settingsView.initializeEnginesDefaultCheckbox(engines.isEmpty())
        Engines.values().forEach { engine ->
            settingsView.initializeEngineCheckBox(engine, engines.contains(engine))
        }
    }

    private fun initializeEnginesSet() {
        engines.clear()
        searchParameter.searchParams.engines
            ?.split(",")
            ?.map { it.trim() }
            ?.forEach { engines.add(Engines.valueOf(it.toUpperCase())) }
    }

    private fun assignSearchParameterEngines() {
        val engineList = arrayListOf<String>()
        engines.forEach { engineList.add(it.urlParameter) }
        searchParameter.searchParams.engines =
                when {
                    engineList.isNullOrEmpty() -> null
                    else -> engineList.joinToString(separator = ",", prefix = "")
                }
    }

    override fun onEngineCheckBoxClick(engine: SettingsActivity.Engines, shouldAdd: Boolean): Boolean {
        if (shouldAdd) engines.add(engine) else engines.remove(engine)
        return engines.isEmpty()
    }

    private fun initializeCategories() {
        initializeCategoriesSet()
        settingsView.initializeCategoriesDefaultCheckbox(categories.isEmpty())
        Categories.values().forEach { category ->
            settingsView.initializeCategoryCheckBox(category, categories.contains(category))
        }
    }

    private fun initializeCategoriesSet() {
        categories.clear()
        searchParameter.searchParams.categories
            ?.split(",")
            ?.map { it.trim() }
            ?.forEach { categories.add(Categories.valueOf(it.toUpperCase())) }
    }

    private fun assignSearchParameterCategories() {
        val categoriesList = arrayListOf<String>()
        categories.forEach { categoriesList.add(it.urlParameter) }
        searchParameter.searchParams.categories =
                when {
                    categoriesList.isNullOrEmpty() -> null
                    else -> categoriesList.joinToString(separator = ",", prefix = "")
                }
    }

    override fun onCategoryCheckBoxClick(category: SettingsActivity.Categories, shouldAdd: Boolean): Boolean {
        if (shouldAdd) categories.add(category) else categories.remove(category)
        return categories.isEmpty()
    }

}
