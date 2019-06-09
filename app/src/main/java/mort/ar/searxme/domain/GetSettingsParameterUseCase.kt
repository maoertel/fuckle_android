package mort.ar.searxme.domain

import io.reactivex.Single
import mort.ar.searxme.data.model.SettingsParameter

interface GetSettingsParameterUseCase {

    fun getSettingsParameter(): Single<SettingsParameter>

}