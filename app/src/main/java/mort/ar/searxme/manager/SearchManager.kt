package mort.ar.searxme.manager

import android.content.Context
import io.reactivex.Observable
import mort.ar.searxme.access.SearxAccess
import mort.ar.searxme.model.SearxResponse
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class SearchManager {

    private val mContext: Context
    private val mSearchParameterManager: SearchParameterManager
    private val mSearxInstanceManager: SearxInstanceManager

    private lateinit var mRetrofitService: SearxAccess

    constructor(context: Context) {
        mContext = context
        mSearchParameterManager = SearchParameterManager(mContext)
        mSearxInstanceManager = SearxInstanceManager(mContext)
        initRetrofitService()
    }

    private fun initRetrofitService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(mSearxInstanceManager.getFirstInstance())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            // .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        mRetrofitService = retrofit.create<SearxAccess>(SearxAccess::class.java)
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
