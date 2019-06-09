package mort.ar.searxme.domain.usecases

import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.domain.GetSettingsParameterUseCase
import mort.ar.searxme.domain.mapper.SettingsParameterMapper
import mort.ar.searxme.data.model.SettingsParameter
import javax.inject.Inject

class GetSettingsParameterUseCaseImpl @Inject constructor(
    private val searxInstanceRepository: SearxInstanceRepository,
    private val searchParameterRepository: SearchParameterRepository,
    private val mapper: SettingsParameterMapper
) : GetSettingsParameterUseCase {

    override fun getSettingsParameter(): Single<SettingsParameter> =
        Singles.zip(
            searxInstanceRepository.getAllInstances(),
            searchParameterRepository.getEngines(),
            searchParameterRepository.getCategories(),
            searchParameterRepository.getLanguage(),
            searchParameterRepository.getTimeRange()
        ) { instances, engines, categories, languages, timeRange ->
            mapper.mapToSettingsParameter(
                searxInstances = instances,
                engines = engines,
                categories = categories,
                language = languages,
                timeRange = timeRange
            )
        }

}