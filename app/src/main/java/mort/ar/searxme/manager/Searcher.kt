package mort.ar.searxme.manager

import io.reactivex.Observable
import io.reactivex.Single
import mort.ar.searxme.access.SearxAccess
import mort.ar.searxme.model.SearxResponse
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject


class Searcher @Inject constructor(
    private val searchParameter: SearchParameter,
    private val searxInstanceBucket: SearxInstanceBucket,
    private val retrofitBuilder: Retrofit.Builder
) {

    private var retrofitService: SearxAccess

    init {
        retrofitService = initRetrofitService()
    }

    private fun initRetrofitService() =
        buildRetrofitService().blockingFirst()

    private fun buildRetrofitService(): Observable<SearxAccess> =
    // Just for logging purposes
    // val httpClient = OkHttpClient.Builder()
    // .readTimeout(30, TimeUnit.SECONDS)
    // .writeTimeout(30, TimeUnit.SECONDS)

    // val logging = HttpLoggingInterceptor()
    // logging.level = HttpLoggingInterceptor.Level.BASIC
    // httpClient.addInterceptor(logging)
        searxInstanceBucket.getPrimaryInstance()
            .flatMap { searxInstance ->
                Observable.just(
                    retrofitBuilder
                        .baseUrl(searxInstance.url)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(MoshiConverterFactory.create())
                        // .client(httpClient.build())
                        .build()
                        .create<SearxAccess>(SearxAccess::class.java)
                )
            }

    fun requestSearchResults(query: String): Observable<SearxResponse> =
        retrofitService.requestSearchResults(
            query = query,
            categories = searchParameter.categories,
            engines = searchParameter.engines,
            language = searchParameter.language.languageParameter,
            pageNo = searchParameter.pageNo,
            timeRange = searchParameter.timeRange.rangeParameter,
            format = searchParameter.format,
            imageProxy = searchParameter.imageProxy,
            autoComplete = searchParameter.autoComplete,
            safeSearch = searchParameter.safeSearch
        )

    fun requestSearchAutoComplete(query: String): Single<Array<String>> =
        retrofitService.requestSearchAutocomplete(query)

    /*    private fun buildSearchRequest(query: String): SearchRequest {
        val searchParams = searchParameter.searchParams
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
