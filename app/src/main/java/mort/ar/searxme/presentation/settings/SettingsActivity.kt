package mort.ar.searxme.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.settings_categories.*
import kotlinx.android.synthetic.main.settings_engines.*
import kotlinx.android.synthetic.main.settings_engines.view.*
import kotlinx.android.synthetic.main.settings_languages.*
import kotlinx.android.synthetic.main.settings_searx_instances.*
import kotlinx.android.synthetic.main.settings_time_ranges.*
import kotlinx.android.synthetic.main.toolbar_settings.*
import mort.ar.searxme.R
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(), SettingsContract.SettingsView {

    @Inject
    lateinit var settingsPresenter: SettingsContract.SettingsPresenter

    @Inject
    lateinit var instanceAdapter: ArrayAdapter<String>
    @Inject
    lateinit var languageAdapter: ArrayAdapter<Languages>
    @Inject
    lateinit var timeRangeAdapter: ArrayAdapter<TimeRanges>


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbarSettings as Toolbar)

        backButton.setOnClickListener { onBackPressed() }

        settingsPresenter.start()
    }

    override fun onResume() {
        super.onResume()
        settingsPresenter.loadSettings()
    }

    override fun onPause() {
        super.onPause()
        settingsPresenter.persistSettings(
            spinnerSearxInstances.selectedItem.toString(),
            spinnerLanguage.selectedItem as Languages,
            spinnerTimeRange.selectedItem as TimeRanges
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsPresenter.stop()
    }

    override fun setSpinnerAdapters() {
        spinnerSearxInstances.adapter = instanceAdapter
        spinnerTimeRange.adapter = timeRangeAdapter
        spinnerLanguage.adapter = languageAdapter
    }

    override fun initializeSearxInstanceSpinner(searxInstances: List<String>) =
        with(instanceAdapter) {
            clear()
            addAll(searxInstances)
            notifyDataSetChanged()
        }

    override fun initializeLanguageSpinner(position: Int) = spinnerLanguage.setSelection(position)

    override fun initializeTimeRangeSpinner(position: Int) = spinnerTimeRange.setSelection(position)

    override fun initializeEnginesDefaultCheckbox(isActivated: Boolean) =
        with(checkBoxDefault) {
            this.activate(isActivated)
            this.setOnClickListener {
                if (checkBoxDefault.isChecked) {
                    settingsPresenter.onEnginesDefaultCheckboxClick()
                }
            }
        }

    override fun initializeEngineCheckBox(engine: Engines, containsEngine: Boolean) {
        findViewById<CheckBox>(engine.checkBox).let { checkbox ->
            checkbox.isChecked = containsEngine
            checkbox.setOnClickListener { settingsPresenter.onEngineCheckBoxClick(engine, checkbox.isChecked) }
        }
    }

    override fun initializeCategoriesDefaultCheckbox(isActivated: Boolean) =
        with(checkBoxCategoriesDefault) {
            this.activate(isActivated)
            this.setOnClickListener {
                if (checkBoxCategoriesDefault.isChecked) {
                    settingsPresenter.onCategoriesDefaultCheckboxClick()
                }
            }
        }

    override fun initializeCategoryCheckBox(category: Categories, containsCategory: Boolean) {
        findViewById<CheckBox>(category.checkBox).let { checkbox ->
            checkbox.isChecked = containsCategory
            checkbox.setOnClickListener { settingsPresenter.onCategoryCheckBoxClick(category, checkbox.isChecked) }
        }
    }

    override fun setEnginesDefaultCheckBoxActive(shouldBeActivated: Boolean) =
        checkBoxDefault.activate(shouldBeActivated)

    override fun setCategoriesDefaultCheckBoxActive(shouldBeActivated: Boolean) =
        checkBoxCategoriesDefault.activate(shouldBeActivated)

    override fun setCheckBoxActive(checkBoxId: Int, shouldBeActivated: Boolean) {
        findViewById<CheckBox>(checkBoxId).isChecked = shouldBeActivated
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(toolbarSettings.windowToken, 0)
    }

}

private fun CheckBox.activate(activate: Boolean) {
    isChecked = activate
    isClickable = !activate
}