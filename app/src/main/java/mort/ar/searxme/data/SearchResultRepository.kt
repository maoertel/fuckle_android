package mort.ar.searxme.data

import io.reactivex.Single
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.presentation.model.SearchRequest

interface SearchResultRepository {

    fun requestSearchResults(searchRequest: SearchRequest): Single<SearchResponse>

    fun requestSearchAutoComplete(searchRequest: SearchRequest): Single<List<String>>

}