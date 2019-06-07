package mort.ar.searxme.domain

import io.reactivex.Single
import mort.ar.searxme.presentation.model.SearchResults

interface SearchRequestUseCase {

    fun requestSearchResults(query: String): Single<SearchResults>

}