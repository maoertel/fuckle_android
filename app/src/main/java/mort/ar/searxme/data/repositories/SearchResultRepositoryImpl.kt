package mort.ar.searxme.data.repositories

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.network.SearchService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val searchParameter: SearchParameterRepositoryImpl,
    private val searxInstanceRepository: SearxInstanceRepository,
    private val retrofitBuilder: Retrofit.Builder,
    private val compositeDisposable: CompositeDisposable,
    private val loggingHttpClient: OkHttpClient
) : SearchResultRepository {

    private lateinit var retrofitService: SearchService

    init {
        compositeDisposable += buildRetrofitService()
            .subscribeOn(Schedulers.io())
            .subscribe { retrofitService = it }
    }

    private fun buildRetrofitService() =
        searxInstanceRepository.observePrimaryInstance()
            .flatMap {
                Observable.just(
                    retrofitBuilder
                        .baseUrl(it.url)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(MoshiConverterFactory.create())
                        .client(loggingHttpClient)
                        .build()
                        .create<SearchService>(SearchService::class.java)
                )
            }

    override fun requestSearchResults(query: String): Observable<SearchResponse> =
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

    override fun requestSearchAutoComplete(query: String): Single<List<String>> =
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