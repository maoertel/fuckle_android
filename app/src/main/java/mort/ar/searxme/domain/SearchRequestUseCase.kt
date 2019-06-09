package mort.ar.searxme.domain

import io.reactivex.Single
import mort.ar.searxme.data.model.SearchResult

interface SearchRequestUseCase {

    fun requestSearchResults(query: String): Single<List<SearchResult>>

}