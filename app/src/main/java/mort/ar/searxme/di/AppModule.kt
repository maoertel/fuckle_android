package mort.ar.searxme.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.data.SearchResultRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.repositories.SearchParameterRepositoryImpl
import mort.ar.searxme.data.repositories.SearchResultRepositoryImpl
import mort.ar.searxme.data.repositories.SearxInstanceRepositoryImpl
import mort.ar.searxme.database.Database
import mort.ar.searxme.database.daos.SearxInstanceDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal class AppModule {

    @Singleton
    @Provides
    fun provideSearchParameterSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("SearchParameter", MODE_PRIVATE)

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
    fun provideSearxInstanceDao(database: Database) =
        database.searxInstanceDao()

    @Provides
    fun provideRetrofitBuilder() = Retrofit.Builder()

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
    fun provideSearcher(
        searchParameter: SearchParameterRepositoryImpl,
        searxInstanceRepositoryImpl: SearxInstanceRepositoryImpl,
        retrofitBuilder: Retrofit.Builder,
        compositeDisposable: CompositeDisposable,
        loggingHttpClient: OkHttpClient
    ): SearchResultRepository =
        SearchResultRepositoryImpl(
            searchParameter,
            searxInstanceRepositoryImpl,
            retrofitBuilder,
            compositeDisposable,
            loggingHttpClient
        )

    @Singleton
    @Provides
    fun provideSearchParameter(sharedPreferences: SharedPreferences): SearchParameterRepositoryImpl = // TODO interface
        SearchParameterRepositoryImpl(sharedPreferences)

    @Singleton
    @Provides
    fun provideSearxInstanceBucket(searxInstanceDao: SearxInstanceDao): SearxInstanceRepository =
        SearxInstanceRepositoryImpl(searxInstanceDao)

    @Provides
    fun provideCompositeDisposables() =
        CompositeDisposable()

}