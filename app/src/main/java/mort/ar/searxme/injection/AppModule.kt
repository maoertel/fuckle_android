package mort.ar.searxme.injection

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import mort.ar.searxme.access.SearxDatabase
import mort.ar.searxme.access.SearxInstanceDao
import mort.ar.searxme.manager.SearchParameter
import mort.ar.searxme.manager.SearchParams
import mort.ar.searxme.manager.Searcher
import mort.ar.searxme.manager.SearxInstanceBucket
import javax.inject.Singleton


@Module
internal class AppModule {

    @Singleton
    @Provides
    fun provideSearchParameterSharedPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences("SearchParameter", MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideDatabase(app: Application): SearxDatabase =
        Room
            .databaseBuilder(
                app,
                SearxDatabase::class.java,
                "searx.db"
            )
            .build()

    @Singleton
    @Provides
    fun provideSearxInstanceDao(database: SearxDatabase) =
        database.searxInstanceDao()

    @Singleton
    @Provides
    fun provideSearcher(
        searchParameter: SearchParameter,
        searxInstanceBucket: SearxInstanceBucket
    ) = Searcher(
        searchParameter,
        searxInstanceBucket
    )

    @Singleton
    @Provides
    fun provideSearchParameter(sharedPreferences: SharedPreferences) =
        SearchParameter(sharedPreferences)

    @Singleton
    @Provides
    fun provideSearxInstanceBucket(searxInstanceDao: SearxInstanceDao) =
        SearxInstanceBucket(searxInstanceDao)

    @Singleton
    @Provides
    fun provideSearchParams(sharedPreferences: SharedPreferences) =
        SearchParams(sharedPreferences)

}
