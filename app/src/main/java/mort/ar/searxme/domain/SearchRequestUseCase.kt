package mort.ar.searxme.domain

import io.reactivex.Observable
import mort.ar.searxme.data.remotedata.model.SearchResponse

interface SearchRequestUseCase {

    fun requestSearchResults(query: String): Observable<SearchResponse>

}