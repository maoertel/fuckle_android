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
import kotlinx.android.synthetic.main.settings_languages.*
import kotlinx.android.synthetic.main.settings_searx_instances.*
import kotlinx.android.synthetic.main.settings_time_ranges.*
import kotlinx.android.synthetic.main.toolbar_settings.*
import mort.ar.searxme.R
import mort.ar.searxme.data.model.SettingsParameter
import mort.ar.searxme.presentation.model.Categories
import mort.ar.searxme.presentation.model.Engines
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
    settingsPresenter.loadSettings()
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

  override infix fun initializeSearxInstanceSpinner(searxInstances: List<String>) = with(instanceAdapter) {
    clear()
    addAll(searxInstances)
    notifyDataSetChanged()
  }

  override infix fun initializeLanguageSpinner(position: Int) = spinnerLanguage.setSelection(position)

  override infix fun initializeTimeRangeSpinner(position: Int) = spinnerTimeRange.setSelection(position)

  override infix fun initializeEnginesDefaultCheckbox(isActivated: Boolean) = with(checkBoxEnginesDefault) {
    this activate isActivated
    this.setOnClickListener { if (checkBoxEnginesDefault.isChecked) settingsPresenter.onEnginesDefaultCheckboxClick() }
  }

  override fun initializeEngineCheckBox(engine: Engines, containsEngine: Boolean) =
    findViewById<CheckBox>(engine.checkBox).let { checkbox ->
      checkbox.isChecked = containsEngine
      checkbox.setOnClickListener {
        settingsPresenter.onEngineCheckBoxClick(
          checkbox.isChecked,
          Engines.values().none { findViewById<CheckBox>(it.checkBox).isChecked }
        )
      }
    }

  override infix fun initializeCategoriesDefaultCheckbox(isActivated: Boolean) = with(checkBoxCategoriesDefault) {
    this activate isActivated
    this.setOnClickListener { if (checkBoxCategoriesDefault.isChecked) settingsPresenter.onCategoriesDefaultCheckboxClick() }
  }

  override fun initializeCategoryCheckBox(category: Categories, containsCategory: Boolean) =
    findViewById<CheckBox>(category.checkBox).let { checkbox ->
      checkbox.isChecked = containsCategory
      checkbox.setOnClickListener {
        settingsPresenter.onCategoryCheckBoxClick(
          checkbox.isChecked,
          Categories.values().none { findViewById<CheckBox>(it.checkBox).isChecked }
        )
      }
    }

  override infix fun setEnginesDefaultCheckBoxActive(shouldBeActivated: Boolean) =
    checkBoxEnginesDefault activate shouldBeActivated

  override infix fun setCategoriesDefaultCheckBoxActive(shouldBeActivated: Boolean) =
    checkBoxCategoriesDefault activate shouldBeActivated

  override fun setCheckBoxActive(checkBoxId: Int, shouldBeActivated: Boolean) {
    findViewById<CheckBox>(checkBoxId).isChecked = shouldBeActivated
  }

  override fun onBackPressed() = settingsPresenter.persistSettings(buildSettingsParameter()) { super.onBackPressed() }

  private fun buildSettingsParameter(): SettingsParameter =
    SettingsParameter(
      searxInstances = listOf(spinnerSearxInstances.selectedItem.toString()),
      engines = Engines.values().filter { findViewById<CheckBox>(it.checkBox).isChecked },
      categories = Categories.values().filter { findViewById<CheckBox>(it.checkBox).isChecked },
      timeRange = spinnerTimeRange.selectedItem as TimeRanges,
      language = spinnerLanguage.selectedItem as Languages
    )

  override infix fun showMessage(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

  override fun hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(toolbarSettings.windowToken, 0)
  }

}

private infix fun CheckBox.activate(activate: Boolean) {
  isChecked = activate
  isClickable = !activate
}