package mort.ar.searxme.data.remotedata.remotedatasources

import io.reactivex.Observable
import io.reactivex.Single
import mort.ar.searxme.data.remotedata.SearchRemoteDataSource
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.network.SearchService
import javax.inject.Inject

class SearchRemoteDataSourceImpl @Inject constructor(
    private val searchService: SearchService
) : SearchRemoteDataSource {

    override fun requestSearchResults(
        query: String,
        categories: String?,
        engines: String?,
        language: String?,
        pageNo: Int?,
        timeRange: String?,
        format: String?,
        imageProxy: String?,
        autoComplete: String?,
        safeSearch: String?
    ): Observable<SearchResponse> =
        searchService.requestSearchResults(
            query,
            categories,
            engines,
            language,
            pageNo,
            timeRange,
            format,
            imageProxy,
            autoComplete,
            safeSearch
        )

    override fun requestSearchAutocomplete(
        query: String?,
        autoComplete: String?
    ): Single<List<String>> = searchService.requestSearchAutocomplete(query, autoComplete)

}