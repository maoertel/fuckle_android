package mort.ar.searxme.settings

import android.widget.ArrayAdapter
import mort.ar.searxme.model.Languages
import mort.ar.searxme.model.TimeRanges


interface SettingsContract {

    interface BaseView {
        fun showProgress()

        fun hideProgress()

        fun showMessage(message: String?)

        fun hideKeyboard()
    }

    interface SettingsView : BaseView {
        fun setSpinnerAdapters(
            instanceAdapter: ArrayAdapter<String>,
            timeRangeAdapter: ArrayAdapter<TimeRanges>,
            languageAdapter: ArrayAdapter<Languages>
        )

        fun initializeSearxInstanceSpinner(position: Int)

        fun initializeTimeRangeSpinner(position: Int)

        fun initializeLanguageSpinner(position: Int)

        fun initializeEnginesDefaultCheckbox(isActivated: Boolean)

        fun setEnginesDefaultCheckBoxActivated()

        fun initializeEngineCheckBox(
            engine: Engines,
            containsEngine: Boolean
        )

        fun initializeCategoriesDefaultCheckbox(isActivated: Boolean)

        fun setCategoriesDefaultCheckBoxActivated()

        fun initializeCategoryCheckBox(
            category: Categories,
            containsCategory: Boolean
        )
    }

    interface BasePresenter {
        fun start()

        fun loadSettings()

        fun persistSettings()

        fun stop()
    }

    interface SettingsPresenter : BasePresenter {
        fun onSearxInstanceSelect(searxInstance: String)

        fun onTimeRangeSelect(selectedTimeRange: TimeRanges)

        fun onLanguageSelect(selectedLanguage: Languages)

        fun onEnginesDefaultCheckboxClick()

        fun onEngineCheckBoxClick(engine: Engines, shouldAdd: Boolean): Boolean

        fun onCategoriesDefaultCheckboxClick()

        fun onCategoryCheckBoxClick(category: Categories, shouldAdd: Boolean): Boolean
    }

}