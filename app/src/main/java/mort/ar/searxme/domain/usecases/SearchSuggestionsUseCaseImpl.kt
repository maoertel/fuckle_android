package mort.ar.searxme.domain.usecases

import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.domain.SearchSuggestionsUseCase
import mort.ar.searxme.presentation.model.SearchRequest
import javax.inject.Inject

class SearchSuggestionsUseCaseImpl @Inject constructor(
    private val searchResultRepository: SearchResultRepository,
    private val searchParameterRepository: SearchParameterRepository

) : SearchSuggestionsUseCase {

    override fun requestSearchAutoComplete(query: String): Single<List<String>> =
        buildAutoCompleteSearchRequest(query)
            .flatMap { searchResultRepository.requestSearchAutoComplete(it) }

    private fun buildAutoCompleteSearchRequest(query: String): Single<SearchRequest> =
        searchParameterRepository
            .getAutoComplete()
            .zipWith(Single.just(query))
            .map { SearchRequest(query = it.second, autoComplete = it.first) }

}