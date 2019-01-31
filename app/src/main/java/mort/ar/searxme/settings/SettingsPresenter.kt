package mort.ar.searxme.settings

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.manager.SearchParameter
import mort.ar.searxme.manager.SearxInstanceBucket
import mort.ar.searxme.model.Languages
import mort.ar.searxme.model.SearxInstance
import mort.ar.searxme.model.TimeRanges
import javax.inject.Inject


class SettingsPresenter @Inject constructor(
    private val settingsView: SettingsContract.SettingsView,
    private val searchParameter: SearchParameter,
    private val searxInstanceBucket: SearxInstanceBucket,
    private val engines: HashSet<Engines>,
    private val categories: HashSet<Categories>,
    private val compositeDisposable: CompositeDisposable
) : SettingsContract.SettingsPresenter {

    override fun start() {
        settingsView.setSpinnerAdapters()
        initializeSearxInstanceSpinner()
    }

    override fun loadSettings() {
        initializeEngines()
        initializeCategories()
        with(settingsView) {
            initializeTimeRangeSpinner(searchParameter.timeRange.ordinal)
            initializeLanguageSpinner(searchParameter.language.ordinal)
        }
    }

    override fun persistSettings(
        instance: String,
        language: Languages,
        timeRange: TimeRanges
    ) {
        persistInstance(instance)
        persistLanguage(language)
        persistTimeRange(timeRange)
//        assignSearchParameterEngines()
//        assignSearchParameterCategories()
        assignSearchParameter { run { searchParameter.engines } }
        assignSearchParameter { run { searchParameter.categories } }
    }

    override fun stop() = compositeDisposable.clear()

    private fun initializeSearxInstanceSpinner() {
        compositeDisposable += searxInstanceBucket.observeAllInstances()
            .flatMap { spinnerSearxInstances -> buildSpinnerInstanceList(spinnerSearxInstances) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { instances -> settingsView.initializeSearxInstanceSpinner(instances) },
                { throwable -> settingsView.showMessage(throwable.message) }
            )
    }

    private fun buildSpinnerInstanceList(spinnerSearxInstances: List<SearxInstance>): Observable<ArrayList<String>>? {
        val instances = arrayListOf<String>()
        val secondaryInstances = arrayListOf<String>()
        spinnerSearxInstances.forEach { searxInstance ->
            when {
                searxInstance.favorite -> instances.add(searxInstance.url)
                else -> secondaryInstances.add(searxInstance.url)
            }
        }
        instances.addAll(secondaryInstances)

        return Observable.just(instances)
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
        searchParameter.engines
            ?.split(",")
            ?.map { it.trim() }
            ?.forEach { engines.add(Engines.valueOf(it.toUpperCase())) }
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
        searchParameter.categories
            ?.split(",")
            ?.map { it.trim() }
            ?.forEach { categories.add(Categories.valueOf(it.toUpperCase())) }
    }

    private fun persistInstance(searxInstance: String) {
        compositeDisposable += searxInstanceBucket.setPrimaryInstance(searxInstance)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { /* do nothing so far */ },
                { throwable -> settingsView.showMessage(throwable.message) }
            )
    }

    override fun onInstanceSpinnerItemSelected(selectedItem: String) {

    }

    private fun persistTimeRange(selectedTimeRange: TimeRanges) {
        searchParameter.timeRange = selectedTimeRange
    }

    private fun persistLanguage(selectedLanguage: Languages) {
        searchParameter.language = selectedLanguage
    }

    override fun onEnginesDefaultCheckboxClick() {
        engines.clear()
        settingsView.setEnginesDefaultCheckBoxActive(true)
        Engines.values().forEach { engine ->
            settingsView.setCheckBoxActive(engine.checkBox, false)
        }
    }

    override fun onEngineCheckBoxClick(engine: Engines, shouldAdd: Boolean) {
        when {
            shouldAdd -> {
                engines.add(engine)
                settingsView.setEnginesDefaultCheckBoxActive(!shouldAdd)
            }
            else -> {
                engines.remove(engine)
                if (engines.isEmpty())
                    settingsView.setEnginesDefaultCheckBoxActive(!shouldAdd)
            }
        }
    }

    override fun onCategoriesDefaultCheckboxClick() {
        categories.clear()
        settingsView.setCategoriesDefaultCheckBoxActive(true)
        Categories.values().forEach { category ->
            settingsView.setCheckBoxActive(category.checkBox, false)
        }
    }

    override fun onCategoryCheckBoxClick(category: Categories, shouldAdd: Boolean) {
        when {
            shouldAdd -> {
                categories.add(category)
                settingsView.setCategoriesDefaultCheckBoxActive(!shouldAdd)
            }
            else -> {
                categories.remove(category)
                if (categories.isEmpty())
                    settingsView.setCategoriesDefaultCheckBoxActive(!shouldAdd)
            }
        }
    }

    private fun assignSearchParameter(parameter: (String?) -> Unit) {
        val engineList = arrayListOf<String>()
        this.engines.forEach { engineList.add(it.urlParameter) }
        parameter(
            when {
                engineList.isNullOrEmpty() -> null
                else -> engineList.joinToString(separator = ",", prefix = "")
            }
        )
    }

    private fun assignSearchParameterEngines() {
        val engineList = arrayListOf<String>()
        engines.forEach { engineList.add(it.urlParameter) }
        searchParameter.engines =
                when {
                    engineList.isNullOrEmpty() -> null
                    else -> engineList.joinToString(separator = ",", prefix = "")
                }
    }

    private fun assignSearchParameterCategories() {
        val categoriesList = arrayListOf<String>()
        categories.forEach { categoriesList.add(it.urlParameter) }
        searchParameter.categories =
                when {
                    categoriesList.isNullOrEmpty() -> null
                    else -> categoriesList.joinToString(separator = ",", prefix = "")
                }
    }

}
