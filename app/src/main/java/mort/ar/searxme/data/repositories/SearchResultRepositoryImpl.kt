package mort.ar.searxme.data.repositories

import io.reactivex.Single
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.remotedata.SearchRemoteDataSource
import mort.ar.searxme.data.model.SearchResult
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: SearchRemoteDataSource
) : SearchResultRepository {

    override fun requestSearchResults(searchRequest: SearchRequest): Single<List<SearchResult>> =
        searchRemoteDataSource.requestSearchResults(searchRequest)

    override fun requestSearchAutoComplete(searchRequest: SearchRequest): Single<List<String>> =
        searchRemoteDataSource.requestSearchAutocomplete(searchRequest)

}