package mort.ar.searxme.domain.usecases

import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.domain.GetSettingsParameterUseCase
import mort.ar.searxme.domain.mapper.SettingsParameterMapper
import mort.ar.searxme.presentation.model.SettingsParameter
import javax.inject.Inject

class GetSettingsParameterUseCaseImpl @Inject constructor(
    private val searxInstanceRepository: SearxInstanceRepository,
    private val searchParameterRepository: SearchParameterRepository
) : GetSettingsParameterUseCase {

    override fun getSettingsParameter(): Single<SettingsParameter> =
        Singles.zip(
            searxInstanceRepository.getAllInstances(),
            searchParameterRepository.getEngines(),
            searchParameterRepository.getCategories(),
            searchParameterRepository.getLanguage(),
            searchParameterRepository.getTimeRange()
        ) { instances, engines, categories, languages, timeRanges ->
            SettingsParameterMapper(instances, engines, categories, languages, timeRanges)
                .mapToSettingsParameter()
        }

}