package mort.ar.searxme.data

import io.reactivex.Single
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.presentation.model.SearchResults

interface SearchResultRepository {

    fun requestSearchResults(searchRequest: SearchRequest): Single<SearchResults>

    fun requestSearchAutoComplete(searchRequest: SearchRequest): Single<List<String>>

}