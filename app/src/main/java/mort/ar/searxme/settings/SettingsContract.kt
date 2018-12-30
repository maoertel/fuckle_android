package mort.ar.searxme.settings


interface SettingsContract {

    interface BaseView {
        fun showProgress()

        fun hideProgress()

        fun showMessage(message: String?)

        fun hideKeyboard()
    }

    interface SettingsView : BaseView {

    }

    interface BasePresenter {
        fun start()

        fun stop()
    }

    interface SettingsPresenter : BasePresenter {

    }

}