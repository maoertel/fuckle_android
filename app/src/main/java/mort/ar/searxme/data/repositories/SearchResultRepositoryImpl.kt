package mort.ar.searxme.data.repositories

import io.reactivex.Single
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.data.remotedata.SearchRemoteDataSource
import mort.ar.searxme.presentation.model.SearchResults
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: SearchRemoteDataSource
) : SearchResultRepository {

    override fun requestSearchResults(searchRequest: SearchRequest): Single<SearchResults> =
        searchRemoteDataSource.requestSearchResults(searchRequest)
            .map { SearchResults(it.results) }

    override fun requestSearchAutoComplete(searchRequest: SearchRequest): Single<List<String>> =
        searchRemoteDataSource.requestSearchAutocomplete(searchRequest)

}