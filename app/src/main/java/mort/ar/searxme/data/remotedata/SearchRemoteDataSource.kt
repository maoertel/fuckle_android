package mort.ar.searxme.data.remotedata

import io.reactivex.Single
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.model.SearchResult

interface SearchRemoteDataSource {

    fun requestSearchResults(searchRequest: SearchRequest): Single<List<SearchResult>>

    fun requestSearchAutocomplete(searchRequest: SearchRequest): Single<List<String>>

}