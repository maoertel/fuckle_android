package mort.ar.searxme.presentation.settings

import mort.ar.searxme.data.model.SettingsParameter
import mort.ar.searxme.presentation.model.Categories
import mort.ar.searxme.presentation.model.Engines

interface SettingsContract {
    interface BaseView {
      infix fun showMessage(message: String?)

        fun hideKeyboard()
    }

    interface SettingsView : BaseView {
        fun setSpinnerAdapters()

      infix fun initializeSearxInstanceSpinner(searxInstances: List<String>)

      infix fun initializeTimeRangeSpinner(position: Int)

      infix fun initializeLanguageSpinner(position: Int)

      infix fun initializeEnginesDefaultCheckbox(isActivated: Boolean)

      infix fun setEnginesDefaultCheckBoxActive(shouldBeActivated: Boolean)

        fun initializeEngineCheckBox(
            engine: Engines,
            containsEngine: Boolean
        )

      infix fun initializeCategoriesDefaultCheckbox(isActivated: Boolean)

      infix fun setCategoriesDefaultCheckBoxActive(shouldBeActivated: Boolean)

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