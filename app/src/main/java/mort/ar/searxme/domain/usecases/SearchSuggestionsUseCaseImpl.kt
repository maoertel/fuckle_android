package mort.ar.searxme.domain.usecases

import io.reactivex.Single
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.domain.SearchSuggestionsUseCase
import javax.inject.Inject

class SearchSuggestionsUseCaseImpl @Inject constructor(
    private val searchResultRepository: SearchResultRepository
) : SearchSuggestionsUseCase {

    override fun requestSearchAutoComplete(query: String): Single<List<String>> =
        searchResultRepository.requestSearchAutoComplete(query)

}