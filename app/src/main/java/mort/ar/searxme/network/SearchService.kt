package mort.ar.searxme.network

import io.reactivex.Observable
import io.reactivex.Single
import mort.ar.searxme.data.remotedata.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("/search")
    fun requestSearchResults(
        @Query("q") query: String,
        @Query("categories") categories: String? = null,
        @Query("engines") engines: String? = null,
        @Query("language") language: String? = null,
        @Query("pageno") pageNo: Int? = null,
        @Query("time_range") timeRange: String? = null,
        @Query("format") format: String? = null,
        @Query("image_proxy") imageProxy: String? = null,
        @Query("autocomplete") autoComplete: String? = null,
        @Query("safesearch") safeSearch: String? = null
    ): Observable<SearchResponse>

    @GET("/autocompleter")
    fun requestSearchAutocomplete(
        @Query("q") query: String? = "",
        @Query("autocomplete") autoComplete: String? = "duckduckgo"
    ): Single<List<String>>

}