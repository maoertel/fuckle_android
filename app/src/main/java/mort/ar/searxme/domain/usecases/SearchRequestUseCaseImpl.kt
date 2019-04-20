package mort.ar.searxme.domain.usecases

import io.reactivex.Observable
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.domain.SearchRequestUseCase
import javax.inject.Inject

class SearchRequestUseCaseImpl @Inject constructor(
    private val searchResultRepository: SearchResultRepository,
    private val searchParameterRepository: SearchParameterRepository
) : SearchRequestUseCase {

    override fun requestSearchResults(query: String): Observable<SearchResponse> =
        searchResultRepository.requestSearchResults(query)

}