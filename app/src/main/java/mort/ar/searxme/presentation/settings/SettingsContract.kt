package mort.ar.searxme.presentation.settings

import mort.ar.searxme.data.model.SettingsParameter
import mort.ar.searxme.presentation.model.Categories
import mort.ar.searxme.presentation.model.Engines

interface SettingsContract {
    interface BaseView {

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

        fun persistSettings(settingsParameter: SettingsParameter, onFinished: () -> Unit)

        fun stop()
    }

    interface SettingsPresenter : BasePresenter {
        fun onEnginesDefaultCheckboxClick()

        fun onEngineCheckBoxClick(
            shouldCheck: Boolean,
            enginesEmpty: Boolean
        )

        fun onCategoriesDefaultCheckboxClick()

        fun onCategoryCheckBoxClick(
            shouldCheck: Boolean,
            categoriesEmpty: Boolean
        )
    }

}