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


class SearchManager {

    private val mSearchParameterManager: SearchParameterManager
    private val mSearxInstanceManager: SearxInstanceManager

    private lateinit var mRetrofitService: SearxAccess

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    @Inject
    constructor(
        searchParameterManager: SearchParameterManager,
        searxInstanceManager: SearxInstanceManager
    ) {
        mSearchParameterManager = searchParameterManager
        mSearxInstanceManager = searxInstanceManager
        initRetrofitService()
    }

    private fun initRetrofitService() {
        mCompositeDisposable += mSearxInstanceManager.getFirstInstance()
            .subscribe { searxInstance ->
                val retrofit = Retrofit.Builder()
                    .baseUrl(searxInstance.url)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    // .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()

                mRetrofitService = retrofit.create<SearxAccess>(SearxAccess::class.java)
            }
    }

    fun getSearchResults(query: String): Observable<SearxResponse> {
        val requestParams = mSearchParameterManager.mSearchParams
        return mRetrofitService.getSearchResults(
            query = query,
            categories = requestParams.categories,
            engines = requestParams.engines,
            language = requestParams.language,
            pageNo = requestParams.pageNo,
            timeRange = requestParams.timeRange,
            format = requestParams.format,
            imageProxy = requestParams.imageProxy,
            autoComplete = requestParams.autoComplete,
            safeSearch = requestParams.safeSearch
        )
    }

    fun requestSearchAutoComplete(query: String): Single<Array<String>> {
        return mRetrofitService.getSearchAutocomplete(query)
    }

    /*    private fun buildSearchRequest(query: String): SearchRequest {
        val searchParams = mSearchParameterManager.mSearchParams
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
