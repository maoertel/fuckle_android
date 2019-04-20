package mort.ar.searxme.domain

import io.reactivex.Single
import mort.ar.searxme.data.remotedata.model.SearchResponse

interface SearchRequestUseCase {

    fun requestSearchResults(query: String): Single<SearchResponse>

}