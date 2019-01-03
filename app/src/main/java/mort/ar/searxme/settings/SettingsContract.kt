package mort.ar.searxme.settings

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
        fun initializeSearxInstanceSpinner(
            instances: ArrayList<String>,
            position: Int
        )

        fun initializeTimeRangeSpinner(position: Int)

        fun initializeLanguageSpinner(position: Int)

        fun initializeEnginesDefaultCheckbox(isActivated: Boolean)

        fun setEnginesDefaultCheckBoxActivated()

        fun setCategoriesDefaultCheckBoxActivated()

        fun initializeEngineCheckBox(
            engine: SettingsActivity.Engines,
            containsEngine: Boolean
        )

        fun initializeCategoriesDefaultCheckbox(isActivated: Boolean)

        fun initializeCategoryCheckBox(
            category: SettingsActivity.Categories,
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

        fun onEngineCheckBoxClick(engine: SettingsActivity.Engines, shouldAdd: Boolean): Boolean

        fun onCategoriesDefaultCheckboxClick()

        fun onCategoryCheckBoxClick(category: SettingsActivity.Categories, shouldAdd: Boolean): Boolean
    }

}