package mort.ar.searxme.data

import io.reactivex.Single
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.model.SearchResult

interface SearchResultRepository {

  fun requestSearchResults(searchRequest: SearchRequest): Single<List<SearchResult>>

  fun requestSearchAutoComplete(searchRequest: SearchRequest): Single<List<String>>

}