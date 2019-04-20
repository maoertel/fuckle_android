package mort.ar.searxme.data.repositories

import io.reactivex.Observable
import io.reactivex.Single
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.network.SearchService
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val searchParameterTemp: SearchParameterRepositoryImplTemp,
    private val searchService: SearchService
) : SearchResultRepository {

    override fun requestSearchResults(query: String): Observable<SearchResponse> =
        searchService.requestSearchResults(
            query = query,
            categories = searchParameterTemp.categories,
            engines = searchParameterTemp.engines,
            language = searchParameterTemp.language.languageParameter,
            pageNo = searchParameterTemp.pageNo,
            timeRange = searchParameterTemp.timeRange.rangeParameter,
            format = searchParameterTemp.format,
            imageProxy = searchParameterTemp.imageProxy,
            autoComplete = searchParameterTemp.autoComplete,
            safeSearch = searchParameterTemp.safeSearch
        )

    override fun requestSearchAutoComplete(query: String): Single<List<String>> =
        searchService.requestSearchAutocomplete(query)

    /*    private fun buildSearchRequest(query: String): SearchRequest {
        val searchParams = searchParameterTemp.searchParams
        return SearchRequest(
            query = query,
            categories = searchParams.categories,
            engines = searchParams.engines,
            language = searchParams.language,
            pageNo = searchParams.pageNo,
            timeRange = searchParams.timeRange,
            format = "json",
            imageProxy = searchParams.imageProxy,
            autoComplete = searchParams.autoComplete,
            safeSearch = searchParams.safeSearch
        )
    }*/

}