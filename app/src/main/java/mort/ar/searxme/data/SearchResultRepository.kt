package mort.ar.searxme.data

import io.reactivex.Observable
import io.reactivex.Single
import mort.ar.searxme.data.remotedata.model.SearchResponse

interface SearchResultRepository {

    fun requestSearchResults(query: String): Observable<SearchResponse>

    fun requestSearchAutoComplete(query: String): Single<List<String>>

}