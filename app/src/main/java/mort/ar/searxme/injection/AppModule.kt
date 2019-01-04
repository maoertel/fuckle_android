package mort.ar.searxme.injection

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.access.Database
import mort.ar.searxme.access.SearxInstanceDao
import mort.ar.searxme.manager.SearchParameter
import mort.ar.searxme.manager.Searcher
import mort.ar.searxme.manager.SearxInstanceBucket
import retrofit2.Retrofit
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

    @Singleton
    @Provides
    fun provideSearcher(
        searchParameter: SearchParameter,
        searxInstanceBucket: SearxInstanceBucket,
        retrofitBuilder: Retrofit.Builder,
        compositeDisposable: CompositeDisposable
    ) =
        Searcher(
            searchParameter,
            searxInstanceBucket,
            retrofitBuilder,
            compositeDisposable
        )

    @Singleton
    @Provides
    fun provideSearchParameter(sharedPreferences: SharedPreferences) =
        SearchParameter(sharedPreferences)

    @Singleton
    @Provides
    fun provideSearxInstanceBucket(searxInstanceDao: SearxInstanceDao) =
        SearxInstanceBucket(searxInstanceDao)

    @Provides
    fun provideCompositeDisposables() =
        CompositeDisposable()

}
