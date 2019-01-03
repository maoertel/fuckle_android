package mort.ar.searxme.settings

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.settings_categories.*
import kotlinx.android.synthetic.main.settings_engines.*
import kotlinx.android.synthetic.main.settings_languages.*
import kotlinx.android.synthetic.main.settings_searx_instances.*
import kotlinx.android.synthetic.main.settings_time_ranges.*
import kotlinx.android.synthetic.main.toolbar_settings.*
import mort.ar.searxme.R
import mort.ar.searxme.R.id.*
import mort.ar.searxme.model.Languages
import mort.ar.searxme.model.TimeRanges
import javax.inject.Inject


class SettingsActivity : AppCompatActivity(), SettingsContract.SettingsView {

    @Inject
    lateinit var settingsPresenter: SettingsContract.SettingsPresenter

    @Inject
    lateinit var timeRangeAdapter: ArrayAdapter<TimeRanges>

    @Inject
    lateinit var languageAdapter: ArrayAdapter<Languages>


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbarSettings as Toolbar)

        backButton.setOnClickListener { onBackPressed() }

        spinnerTimeRange.adapter = timeRangeAdapter
        spinnerLanguage.adapter = languageAdapter

        settingsPresenter.start()
    }

    override fun onResume() {
        super.onResume()
        settingsPresenter.loadSettings()
    }

    override fun onPause() {
        super.onPause()
        settingsPresenter.persistSettings()
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsPresenter.stop()
    }

    override fun initializeSearxInstanceSpinner(instances: ArrayList<String>, position: Int) {
        spinnerSearxInstances.adapter =
                ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    instances
                )
        spinnerSearxInstances.setSelection(position)
        spinnerSearxInstances.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                settingsPresenter.onSearxInstanceSelect(spinnerSearxInstances.selectedItem.toString())

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun initializeLanguageSpinner(position: Int) {
        spinnerLanguage.setSelection(position)
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                settingsPresenter.onLanguageSelect(spinnerLanguage.selectedItem as Languages)

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun initializeTimeRangeSpinner(position: Int) {
        spinnerTimeRange.setSelection(position)
        spinnerTimeRange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                settingsPresenter.onTimeRangeSelect(spinnerTimeRange.selectedItem as TimeRanges)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun initializeEnginesDefaultCheckbox(isActivated: Boolean) {
        checkBoxDefault.activate(isActivated)
        checkBoxDefault.setOnClickListener {
            if (checkBoxDefault.isChecked) {
                checkBoxDefault.isClickable = false
                Engines.values().forEach { engine ->
                    findViewById<CheckBox>(engine.checkBox).isChecked = false
                }
            }
        }
    }

    override fun initializeEngineCheckBox(engine: Engines, containsEngine: Boolean) {
        val checkbox = findViewById<CheckBox>(engine.checkBox)
        checkbox.isChecked = containsEngine
        checkbox.setOnClickListener {
            val clickClearedEngines =
                settingsPresenter.onEngineCheckBoxClick(engine, checkbox.isChecked)
            when (checkbox.isChecked) {
                true -> checkBoxDefault.activate(!checkbox.isChecked)
                false -> if (clickClearedEngines) checkBoxDefault.activate(!checkbox.isChecked)
            }
        }
    }

    override fun initializeCategoriesDefaultCheckbox(isActivated: Boolean) {
        checkBoxCategoriesDefault.activate(isActivated)
        checkBoxCategoriesDefault.setOnClickListener {
            if (checkBoxCategoriesDefault.isChecked) {
                checkBoxCategoriesDefault.isClickable = false
                Categories.values().forEach { category ->
                    findViewById<CheckBox>(category.checkBox).isChecked = false
                }
            }
        }
    }

    override fun initializeCategoryCheckBox(category: Categories, containsCategory: Boolean) {
        val checkbox = findViewById<CheckBox>(category.checkBox)

        checkbox.isChecked = containsCategory
        checkbox.setOnClickListener {
            val clickClearedCategories =
                settingsPresenter.onCategoryCheckBoxClick(category, checkbox.isChecked)
            when (checkbox.isChecked) {
                true -> checkBoxCategoriesDefault.activate(!checkbox.isChecked)
                false -> if (clickClearedCategories) checkBoxCategoriesDefault.activate(!checkbox.isChecked)
            }
        }
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(toolbar.windowToken, 0)
    }

    enum class Engines(
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

    enum class Categories(
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
