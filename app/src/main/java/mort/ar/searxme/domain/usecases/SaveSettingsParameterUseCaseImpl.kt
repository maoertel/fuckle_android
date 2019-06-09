package mort.ar.searxme.domain.usecases

import io.reactivex.Completable
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.di.DataModule
import mort.ar.searxme.domain.SaveSettingsParameterUseCase
import mort.ar.searxme.data.model.SettingsParameter
import mort.ar.searxme.presentation.settings.Categories
import mort.ar.searxme.presentation.settings.Engines
import javax.inject.Inject

class SaveSettingsParameterUseCaseImpl @Inject constructor(
    private val searxInstanceRepository: SearxInstanceRepository,
    private val searchParameterRepository: SearchParameterRepository
) : SaveSettingsParameterUseCase {

    override fun saveSettingsParameter(settingsParameter: SettingsParameter): Completable =
        Completable.merge(
            listOf(
                searxInstanceRepository.setPrimaryInstance(settingsParameter.searxInstances.first()),
                saveEngines(settingsParameter.engines),
                saveCategories(settingsParameter.categories),
                searchParameterRepository.setLanguage(settingsParameter.language),
                searchParameterRepository.setTimeRange(settingsParameter.timeRange)
            )
        ).also { DataModule.currentHost = settingsParameter.searxInstances.first() }

    private fun saveEngines(engines: List<Engines>) =
        searchParameterRepository.setEngines(engines.joinToString(separator = ",") { it.urlParameter })

    private fun saveCategories(categories: List<Categories>): Completable =
        searchParameterRepository.setCategories(categories.joinToString(separator = ",") { it.urlParameter })

}