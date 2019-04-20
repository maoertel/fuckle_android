package mort.ar.searxme.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.localdata.SearchParameterDataSource
import mort.ar.searxme.data.localdata.SearxInstanceDataSource
import mort.ar.searxme.data.localdata.localdatasources.SearchParameterDataSourceImpl
import mort.ar.searxme.data.localdata.localdatasources.SearxInstanceDataSourceImpl
import mort.ar.searxme.data.repositories.SearchParameterRepositoryImplTemp
import mort.ar.searxme.data.repositories.SearchResultRepositoryImpl
import mort.ar.searxme.data.repositories.SearxInstanceRepositoryImpl
import mort.ar.searxme.database.Database
import mort.ar.searxme.database.daos.SearxInstanceDao
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("SearchParameter", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSearxInstanceDao(database: Database) =
        database.searxInstanceDao()

    @Singleton
    @Provides
    fun provideSearchResultRepository(
        searchParameterTemp: SearchParameterRepositoryImplTemp,
        searxInstanceRepositoryImpl: SearxInstanceRepositoryImpl,
        retrofitBuilder: Retrofit.Builder,
        compositeDisposable: CompositeDisposable,
        loggingHttpClient: OkHttpClient
    ): SearchResultRepository =
        SearchResultRepositoryImpl(
            searchParameterTemp,
            searxInstanceRepositoryImpl,
            retrofitBuilder,
            compositeDisposable,
            loggingHttpClient
        )

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

}