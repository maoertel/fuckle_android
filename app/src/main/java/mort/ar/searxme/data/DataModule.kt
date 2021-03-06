package mort.ar.searxme.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import mort.ar.searxme.data.localdata.SearchParameterDataSource
import mort.ar.searxme.data.localdata.SearxInstanceDataSource
import mort.ar.searxme.data.localdata.localdatasources.SearchParameterDataSourceImpl
import mort.ar.searxme.data.localdata.localdatasources.SearxInstanceDataSourceImpl
import mort.ar.searxme.data.localdata.mapper.SearchInstanceMapper
import mort.ar.searxme.data.mapper.SearchRequestMapper
import mort.ar.searxme.data.mapper.SettingsParameterMapper
import mort.ar.searxme.data.remotedata.SearchRemoteDataSource
import mort.ar.searxme.data.remotedata.mapper.SearchResultMapper
import mort.ar.searxme.data.remotedata.remotedatasources.SearchRemoteDataSourceImpl
import mort.ar.searxme.data.repositories.SearchParameterRepositoryImpl
import mort.ar.searxme.data.repositories.SearchResultRepositoryImpl
import mort.ar.searxme.data.repositories.SearxInstanceRepositoryImpl
import mort.ar.searxme.database.Database
import mort.ar.searxme.database.daos.SearxInstanceDao
import mort.ar.searxme.network.SearchService
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class DataModule {

    companion object HostSettings {
        private const val SCHEME_HTTP = "http"
        private const val SCHEME_HTTPS = "https"

        var currentHost: String = "should not matter"
            set(value) {
                field = when {
                    value.startsWith(SCHEME_HTTPS) -> value.drop(8)
                    value.startsWith(SCHEME_HTTP) -> value.drop(7)
                    else -> value
                }
            }
    }

    @Singleton
    @Provides
    fun provideSearchService(loggingHttpClient: OkHttpClient): SearchService =
        Retrofit.Builder()
            .baseUrl("https://localhost()")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .client(loggingHttpClient)
            .build()
            .create<SearchService>(SearchService::class.java)

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val httpClient =
            OkHttpClient.Builder()
                .addInterceptor {
                    val request = it.request()
                    val url: HttpUrl = request.url
                        .newBuilder()
                        .scheme(SCHEME_HTTPS)
                        .host(currentHost)
                        .build()
                    it.proceed(
                        request.newBuilder()
                            .url(url)
                            .build()
                    )
                }
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        httpClient.addInterceptor(logging)

        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("SearchParameter", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideDatabase(application: Application): Database = Room
        .databaseBuilder(
            application,
            Database::class.java,
            "searx.db"
        )
        .build()

    @Singleton
    @Provides
    fun provideSearxInstanceDao(database: Database) = database.searxInstanceDao()

    @Singleton
    @Provides
    fun providesSearchParameterRepository(searchParameterDataSource: SearchParameterDataSource): SearchParameterRepository =
        SearchParameterRepositoryImpl(searchParameterDataSource)

    @Singleton
    @Provides
    fun provideSearchResultRepository(searchRemoteDataSource: SearchRemoteDataSource): SearchResultRepository =
        SearchResultRepositoryImpl(searchRemoteDataSource)

    @Singleton
    @Provides
    fun provideSearxInstanceRepository(searxInstanceDataSource: SearxInstanceDataSource): SearxInstanceRepository =
        SearxInstanceRepositoryImpl(searxInstanceDataSource)

    @Singleton
    @Provides
    fun provideSearxInstanceDataSource(
        searxInstanceDao: SearxInstanceDao,
        searchInstanceMapper: SearchInstanceMapper
    ): SearxInstanceDataSource = SearxInstanceDataSourceImpl(searxInstanceDao, searchInstanceMapper)

    @Singleton
    @Provides
    fun provideSearchParameterDataSource(sharedPreferences: SharedPreferences): SearchParameterDataSource =
        SearchParameterDataSourceImpl(sharedPreferences)

    @Singleton
    @Provides
    fun provideSearchRemoteDataSource(
        searchService: SearchService,
        searchResultMapper: SearchResultMapper
    ): SearchRemoteDataSource = SearchRemoteDataSourceImpl(searchService, searchResultMapper)

    @Singleton
    @Provides
    fun provideSearchRequestMapper(): SearchRequestMapper = SearchRequestMapper()

    @Singleton
    @Provides
    fun provideSearchResultMapper(): SearchResultMapper = SearchResultMapper()

    @Singleton
    @Provides
    fun provideSearchInstanceMapper(): SearchInstanceMapper = SearchInstanceMapper()

    @Singleton
    @Provides
    fun provideSettingsParameterMapper(): SettingsParameterMapper = SettingsParameterMapper()

}