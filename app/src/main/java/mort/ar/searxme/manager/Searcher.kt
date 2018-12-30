package mort.ar.searxme.manager

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import mort.ar.searxme.access.SearxAccess
import mort.ar.searxme.model.SearxResponse
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject


class Searcher @Inject constructor(
    private val searchParameter: SearchParameter,
    private val searxInstanceBucket: SearxInstanceBucket
) {

    private lateinit var retrofitService: SearxAccess

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        initRetrofitService()
    }

    private fun initRetrofitService() {
        compositeDisposable += searxInstanceBucket.getCurrentInstance()
            .subscribe { searxInstance ->
                val retrofit = Retrofit.Builder()
                    .baseUrl(searxInstance.url)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    // .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()

                retrofitService = retrofit.create<SearxAccess>(SearxAccess::class.java)
            }
    }

    fun requestSearchResults(query: String): Observable<SearxResponse> {
        val requestParams = searchParameter.searchParams
        return retrofitService.requestSearchResults(
            query = query,
            categories = requestParams.categories,
            engines = requestParams.engines,
            language = requestParams.language.languageParameter,
            pageNo = requestParams.pageNo,
            timeRange = requestParams.timeRange.rangeParameter,
            format = requestParams.format,
            imageProxy = requestParams.imageProxy,
            autoComplete = requestParams.autoComplete,
            safeSearch = requestParams.safeSearch
        )
    }

    fun requestSearchAutoComplete(query: String): Single<Array<String>> {
        return retrofitService.requestSearchAutocomplete(query)
    }

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
