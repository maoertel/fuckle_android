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
        fun setSpinnerAdapters()

        fun initializeSearxInstanceSpinner(searxInstances: List<String>)

        fun initializeTimeRangeSpinner(position: Int)

        fun initializeLanguageSpinner(position: Int)

        fun initializeEnginesDefaultCheckbox(isActivated: Boolean)

        fun setEnginesDefaultCheckBoxActive(shouldBeActivated: Boolean)

        fun initializeEngineCheckBox(
            engine: Engines,
            containsEngine: Boolean
        )

        fun initializeCategoriesDefaultCheckbox(isActivated: Boolean)

        fun setCategoriesDefaultCheckBoxActive(shouldBeActivated: Boolean)

        fun initializeCategoryCheckBox(
            category: Categories,
            containsCategory: Boolean
        )

        fun setCheckBoxActive(checkBoxId: Int, shouldBeActivated: Boolean)
    }

    interface BasePresenter {
        fun start()

        fun loadSettings()

        fun persistSettings(
            instance: String,
            language: Languages,
            timeRange: TimeRanges
        )

        fun stop()
    }

    interface SettingsPresenter : BasePresenter {
        fun onEnginesDefaultCheckboxClick()

        fun onEngineCheckBoxClick(
            engine: Engines,
            shouldAdd: Boolean
        )

        fun onCategoriesDefaultCheckboxClick()

        fun onCategoryCheckBoxClick(
            category: Categories,
            shouldAdd: Boolean
        )
    }

}
