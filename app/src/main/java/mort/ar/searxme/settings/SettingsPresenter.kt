package mort.ar.searxme.settings

import javax.inject.Inject


class SettingsPresenter @Inject constructor(
    private val settingsView: SettingsContract.SettingsView
) : SettingsContract.SettingsPresenter {

    override fun start() {
    }

    override fun stop() {
    }

}
