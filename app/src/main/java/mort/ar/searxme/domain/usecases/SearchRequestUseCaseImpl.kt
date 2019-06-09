package mort.ar.searxme.domain.usecases

import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.mapper.SearchRequestMapper
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.domain.SearchRequestUseCase
import mort.ar.searxme.data.model.SearchResult
import javax.inject.Inject

class SearchRequestUseCaseImpl @Inject constructor(
    private val searchResultRepository: SearchResultRepository,
    private val searchParameterRepository: SearchParameterRepository,
    private val mapper: SearchRequestMapper
) : SearchRequestUseCase {

    override fun requestSearchResults(query: String): Single<List<SearchResult>> =
        buildSearchRequest(query).flatMap { searchResultRepository.requestSearchResults(it) }

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
            mapper.mapToSearchRequest(
                query = query,
                categories = cat,
                engines = eng,
                language = lang,
                pageNo = pageNo,
                timeRange = time,
                format = format,
                imageProxy = imgProxy,
                autoComplete = auto,
                safeSearch = safe
            )
        }

}