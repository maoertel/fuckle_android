package mort.ar.searxme.data.remotedata.remotedatasources

import io.reactivex.Single
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.model.SearchResult
import mort.ar.searxme.data.remotedata.SearchRemoteDataSource
import mort.ar.searxme.data.remotedata.mapper.SearchResultMapper
import mort.ar.searxme.network.SearchService
import javax.inject.Inject

class SearchRemoteDataSourceImpl @Inject constructor(
  private val searchService: SearchService,
  private val mapper: SearchResultMapper
) : SearchRemoteDataSource {

  override fun requestSearchResults(searchRequest: SearchRequest): Single<List<SearchResult>> =
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
    ).map { it.results.map { searxResult -> mapper mapFromSearxResult searxResult } }

  override fun requestSearchAutocomplete(searchRequest: SearchRequest): Single<List<String>> =
    searchService.requestSearchAutocomplete(
      query = searchRequest.query,
      autoComplete = if (searchRequest.autoComplete.isNullOrBlank()) null else searchRequest.autoComplete
    )

}