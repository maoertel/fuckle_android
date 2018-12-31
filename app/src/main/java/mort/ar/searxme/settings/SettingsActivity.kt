package mort.ar.searxme.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.settings_engines.*
import kotlinx.android.synthetic.main.settings_languages.*
import kotlinx.android.synthetic.main.settings_searx_instances.*
import kotlinx.android.synthetic.main.settings_time_ranges.*
import kotlinx.android.synthetic.main.toolbar_settings.*
import mort.ar.searxme.R
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

    private val engines = HashMap<String, String>()


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

    private fun initializeCategories() {

    }

    private fun initializeEngines() {
        checkBoxDefault.isChecked = true
        checkBoxDefault.setOnClickListener {
            when (checkBoxDefault.isChecked) {
                true -> checkBoxDefault.isChecked = false
                else -> checkBoxDefault.isChecked = true
            }
        }

        checkBoxDdg.setOnClickListener {
            when (checkBoxDdg.isChecked) {
                true -> engines["duckduckgo"] = "duckduckgo"
                else -> engines.remove("duckduckgo")
            }
        }

        checkBoxBing.setOnClickListener {
            when (checkBoxBing.isChecked) {
                true -> engines["bing"] = "bing"
                else -> engines.remove("bing")
            }
        }

        checkBoxWikipedia.setOnClickListener {
            when (checkBoxWikipedia.isChecked) {
                true -> engines["wikipedia"] = "wikipedia"
                else -> engines.remove("wikipedia")
            }
        }

        checkBoxDuden.setOnClickListener {
            when (checkBoxDuden.isChecked) {
                true -> engines["duden"] = "duden"
                else -> engines.remove("duden")
            }
        }

        checkBoxGoogle.setOnClickListener {
            when (checkBoxGoogle.isChecked) {
                true -> engines["google"] = "google"
                else -> engines.remove("google")
            }
        }

        checkBoxQwant.setOnClickListener {
            when (checkBoxQwant.isChecked) {
                true -> engines["qwant"] = "qwant"
                else -> engines.remove("qwant")
            }
        }
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
        searchParameter.searchParams.engines = engines.values.joinToString(",")
        super.onBackPressed()
    }

}
