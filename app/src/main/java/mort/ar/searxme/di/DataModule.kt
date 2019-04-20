package mort.ar.searxme.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.localdata.SearchParameterDataSource
import mort.ar.searxme.data.localdata.SearxInstanceDataSource
import mort.ar.searxme.data.localdata.localdatasources.SearchParameterDataSourceImpl
import mort.ar.searxme.data.localdata.localdatasources.SearxInstanceDataSourceImpl
import mort.ar.searxme.data.remotedata.SearchRemoteDataSource
import mort.ar.searxme.data.remotedata.remotedatasources.SearchRemoteDataSourceImpl
import mort.ar.searxme.data.repositories.SearchParameterRepositoryImpl
import mort.ar.searxme.data.repositories.SearchResultRepositoryImpl
import mort.ar.searxme.data.repositories.SearxInstanceRepositoryImpl
import mort.ar.searxme.database.Database
import mort.ar.searxme.database.daos.SearxInstanceDao
import mort.ar.searxme.network.SearchService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideSearchService(loggingHttpClient: OkHttpClient): SearchService =
        Retrofit.Builder()
            .baseUrl("https://anonyk.com/")
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
    fun provideDatabase(application: Application): Database =
        Room
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
    fun provideSearxInstanceDataSource(searxInstanceDao: SearxInstanceDao): SearxInstanceDataSource =
        SearxInstanceDataSourceImpl(searxInstanceDao)

    @Singleton
    @Provides
    fun provideSearchParameterDataSource(sharedPreferences: SharedPreferences): SearchParameterDataSource =
        SearchParameterDataSourceImpl(sharedPreferences)

    @Singleton
    @Provides
    fun provideSearchRemoteDataSource(searchService: SearchService): SearchRemoteDataSource =
        SearchRemoteDataSourceImpl(searchService)

}