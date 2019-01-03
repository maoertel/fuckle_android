package mort.ar.searxme.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.settings_categories.*
import kotlinx.android.synthetic.main.settings_engines.*
import kotlinx.android.synthetic.main.settings_languages.*
import kotlinx.android.synthetic.main.settings_searx_instances.*
import kotlinx.android.synthetic.main.settings_time_ranges.*
import kotlinx.android.synthetic.main.toolbar_settings.*
import mort.ar.searxme.R
import mort.ar.searxme.R.id.*
import mort.ar.searxme.manager.SearchParameter
import mort.ar.searxme.manager.SearxInstanceBucket
import mort.ar.searxme.model.Languages
import mort.ar.searxme.model.TimeRanges
import javax.inject.Inject


class SettingsActivity : AppCompatActivity(), SettingsContract.SettingsView {

    @Inject
    lateinit var settingsPresenter: SettingsContract.SettingsPresenter

    @Inject
    lateinit var searchParameter: SearchParameter

    @Inject
    lateinit var searxInstanceBucket: SearxInstanceBucket

    private val compositeDisposable = CompositeDisposable()

    private val engines = HashSet<Engines>()
    private val categories = HashSet<Categories>()


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbarSettings as Toolbar)

        backButton.setOnClickListener { onBackPressed() }

        initializeSearxInstanceSpinner()
        initializeLanguageSpinner()
        initializeTimeRangeSpinner()
        initializeCategories()
        initializeEngines()
    }

    private fun initializeSearxInstanceSpinner() {
        compositeDisposable += searxInstanceBucket.getAllInstances()
            .flatMap { spinnerSearxInstances ->
                val instances = arrayListOf<String>()
                spinnerSearxInstances.forEach { instances.add(it.url) }
                Observable.just(instances)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { instances ->
                spinnerSearxInstances.adapter = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    instances
                )
                spinnerSearxInstances.setSelection(0)
                spinnerSearxInstances.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        searxInstanceBucket.setCurrentInstance(spinnerSearxInstances.selectedItem.toString())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
    }

    private fun initializeLanguageSpinner() {
        spinnerLanguage.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            Languages.values()
        )
        spinnerLanguage.setSelection(searchParameter.searchParams.language.ordinal)
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                searchParameter.searchParams.language = spinnerLanguage.selectedItem as Languages
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initializeTimeRangeSpinner() {
        spinnerTimeRange.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            TimeRanges.values()
        )
        spinnerTimeRange.setSelection(searchParameter.searchParams.timeRange.ordinal)
        spinnerTimeRange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                searchParameter.searchParams.timeRange = spinnerTimeRange.selectedItem as TimeRanges
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initializeEngines() {
        initializeEnginesSet()
        initializeEnginesDefaultCheckbox()
        initializeEngineCheckBoxes()
    }

    private fun initializeCategories() {
        initializeCategoriesSet()
        initializeCategoriesDefaultCheckbox()
        initializeCategoriesCheckBoxes()

    }

    private fun initializeCategoriesSet() {
        categories.clear()
        searchParameter.searchParams.categories
            ?.split(",")
            ?.map { it.trim() }
            ?.forEach { categories.add(Categories.valueOf(it.toUpperCase())) }
    }

    private fun initializeEnginesSet() {
        engines.clear()
        searchParameter.searchParams.engines
            ?.split(",")
            ?.map { it.trim() }
            ?.forEach { engines.add(Engines.valueOf(it.toUpperCase())) }
    }

    private fun initializeEnginesDefaultCheckbox() {
        activateCheckBoxEnginesDefault(engines.isEmpty())
        checkBoxDefault.setOnClickListener {
            if (checkBoxDefault.isChecked) {
                engines.clear()
                checkBoxDefault.isClickable = false
                Engines.values().forEach { engine ->
                    findViewById<CheckBox>(engine.checkBox).isChecked = false
                }
            }
        }
    }

    private fun initializeCategoriesDefaultCheckbox() {
        activateCheckBoxCategoriesDefault(categories.isEmpty())
        checkBoxCategoriesDefault.setOnClickListener {
            if (checkBoxCategoriesDefault.isChecked) {
                categories.clear()
                checkBoxCategoriesDefault.isClickable = false
                Categories.values().forEach { category ->
                    findViewById<CheckBox>(category.checkBox).isChecked = false
                }
            }
        }
    }

    private fun initializeEngineCheckBoxes() {
        Engines.values().forEach { engine ->
            val checkbox = findViewById<CheckBox>(engine.checkBox)

            checkbox.isChecked = engines.contains(engine)
            checkbox.setOnClickListener {
                when (checkbox.isChecked) {
                    true -> {
                        engines.add(engine)
                        activateCheckBoxEnginesDefault(!checkbox.isChecked)
                    }
                    false -> {
                        engines.remove(engine)
                        if (engines.isEmpty())
                            activateCheckBoxEnginesDefault(!checkbox.isChecked)

                    }
                }
            }
        }
    }

    private fun initializeCategoriesCheckBoxes() {
        Categories.values().forEach { category ->
            val checkbox = findViewById<CheckBox>(category.checkBox)

            checkbox.isChecked = categories.contains(category)
            checkbox.setOnClickListener {
                when (checkbox.isChecked) {
                    true -> {
                        categories.add(category)
                        activateCheckBoxCategoriesDefault(!checkbox.isChecked)
                    }
                    false -> {
                        categories.remove(category)
                        if (categories.isEmpty())
                            activateCheckBoxCategoriesDefault(!checkbox.isChecked)

                    }
                }
            }
        }
    }

    private fun activateCheckBoxEnginesDefault(activate: Boolean) {
        checkBoxDefault.isChecked = activate
        checkBoxDefault.isClickable = !activate
    }

    private fun activateCheckBoxCategoriesDefault(activate: Boolean) {
        checkBoxCategoriesDefault.isChecked = activate
        checkBoxCategoriesDefault.isClickable = !activate
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showMessage(message: String?) {
    }

    override fun hideKeyboard() {
    }

    override fun onBackPressed() {
        assignSearchParameterEngines()
        assignSearchParameterCategories()
        super.onBackPressed()
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

    private fun assignSearchParameterCategories() {
        val categoriesList = arrayListOf<String>()
        categories.forEach { categoriesList.add(it.urlParameter) }
        searchParameter.searchParams.categories =
                when {
                    categoriesList.isNullOrEmpty() -> null
                    else -> categoriesList.joinToString(separator = ",", prefix = "")
                }
    }

    private enum class Engines(
        val urlParameter: String,
        val checkBox: Int
    ) {
        DUCKDUCKGO("duckduckgo", checkBoxDdg),
        WIKIPEDIA("wikipedia", checkBoxWikipedia),
        DUDEN("duden", checkBoxDuden),
        BING("bing", checkBoxBing),
        GOOGLE("google", checkBoxGoogle),
        QWANT("qwant", checkBoxQwant)
    }

    private enum class Categories(
        val urlParameter: String,
        val checkBox: Int
    ) {
        FILES("files", checkBoxFiles),
        IMAGES("images", checkBoxImages),
        IT("it", checkBoxIT),
        MAPS("maps", checkBoxMap),
        MUSIC("music", checkBoxMusic),
        NEWS("news", checkBoxNews),
        SCIENCE("science", checkBoxScience),
        SOCIAL_MEDIA("social_media", checkBoxSocialMedia),
        VIDEOS("videos", checkBoxVideo)
    }

}

fun CheckBox.activate(activate: Boolean) {
    isChecked = activate
    isClickable = !activate
}