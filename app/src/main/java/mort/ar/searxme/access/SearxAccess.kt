package mort.ar.searxme.access

import io.reactivex.Observable
import io.reactivex.Single
import mort.ar.searxme.model.SearxResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface SearxAccess {

    @GET("/search")
    fun getSearchResults(
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
    ): Observable<SearxResponse>

    @GET("/autocompleter")
    fun getSearchAutocomplete(
        @Query("q") query: String? = "",
        @Query("autocomplete") autoComplete: String? = "duckduckgo"
    ): Single<Array<String>>

}