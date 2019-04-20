package mort.ar.searxme.data.remotedata

import io.reactivex.Observable
import io.reactivex.Single
import mort.ar.searxme.data.remotedata.model.SearchResponse

interface SearchRemoteDataSource {

    fun requestSearchResults(
        query: String,
        categories: String? = null,
        engines: String? = null,
        language: String? = null,
        pageNo: Int? = null,
        timeRange: String? = null,
        format: String? = null,
        imageProxy: String? = null,
        autoComplete: String? = null,
        safeSearch: String? = null
    ): Observable<SearchResponse>

    fun requestSearchAutocomplete(query: String?, autoComplete: String?): Single<List<String>>
}