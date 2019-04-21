package mort.ar.searxme.data

import io.reactivex.Single
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.remotedata.model.SearchResponse

interface SearchResultRepository {

    fun requestSearchResults(searchRequest: SearchRequest): Single<SearchResponse>

    fun requestSearchAutoComplete(searchRequest: SearchRequest): Single<List<String>>

}