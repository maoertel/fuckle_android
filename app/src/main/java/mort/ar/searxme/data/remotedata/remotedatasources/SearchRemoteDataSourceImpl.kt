package mort.ar.searxme.data.remotedata.remotedatasources

import io.reactivex.Single
import mort.ar.searxme.data.remotedata.SearchRemoteDataSource
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.network.SearchService
import mort.ar.searxme.presentation.model.SearchRequest
import javax.inject.Inject

class SearchRemoteDataSourceImpl @Inject constructor(
    private val searchService: SearchService
) : SearchRemoteDataSource {

    override fun requestSearchResults(searchRequest: SearchRequest): Single<SearchResponse> =
        searchService.requestSearchResults(
            query = searchRequest.query,
            categories = searchRequest.categories,
            engines = searchRequest.engines,
            language = searchRequest.language,
            pageNo = searchRequest.pageNo,
            timeRange = searchRequest.timeRange,
            format = searchRequest.format,
            imageProxy = searchRequest.imageProxy,
            autoComplete = searchRequest.autoComplete,
            safeSearch = searchRequest.safeSearch
        )

    override fun requestSearchAutocomplete(searchRequest: SearchRequest): Single<List<String>> =
        searchService.requestSearchAutocomplete(query = searchRequest.query, autoComplete = searchRequest.autoComplete)

}