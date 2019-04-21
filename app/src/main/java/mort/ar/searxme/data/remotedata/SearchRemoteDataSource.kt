package mort.ar.searxme.data.remotedata

import io.reactivex.Single
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.remotedata.model.SearchResponse

interface SearchRemoteDataSource {

    fun requestSearchResults(searchRequest: SearchRequest): Single<SearchResponse>

    fun requestSearchAutocomplete(searchRequest: SearchRequest): Single<List<String>>
}