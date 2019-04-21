package mort.ar.searxme.domain

import io.reactivex.Completable
import mort.ar.searxme.presentation.model.SettingsParameter

interface SaveSettingsParameterUseCase {

    fun saveSettingsParameter(settingsParameter: SettingsParameter): Completable

}