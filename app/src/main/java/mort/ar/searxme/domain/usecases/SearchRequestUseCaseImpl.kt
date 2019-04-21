package mort.ar.searxme.domain.usecases

import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.mapper.SearchRequestMapper
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.domain.SearchRequestUseCase
import javax.inject.Inject

class SearchRequestUseCaseImpl @Inject constructor(
    private val searchResultRepository: SearchResultRepository,
    private val searchParameterRepository: SearchParameterRepository
) : SearchRequestUseCase {

    override fun requestSearchResults(query: String): Single<SearchResponse> =
        buildSearchRequest(query)
            .flatMap { searchResultRepository.requestSearchResults(it) }

    private fun buildSearchRequest(query: String): Single<SearchRequest> =
        Singles.zip(
            searchParameterRepository.getCategories(),
            searchParameterRepository.getEngines(),
            searchParameterRepository.getLanguage(),
            searchParameterRepository.getPageNo(),
            searchParameterRepository.getTimeRange(),
            searchParameterRepository.getFormat(),
            searchParameterRepository.getImageProxy(),
            searchParameterRepository.getAutoComplete(),
            searchParameterRepository.getSafeSearch()
        ) { cat, eng, lang, pageNo, time, format, imgProxy, auto, safe ->
            SearchRequestMapper(query, cat, eng, lang, pageNo, time, format, imgProxy, auto, safe)
                .mapToSearchRequest()
        }

}