package mort.ar.searxme.injection

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import mort.ar.searxme.TextWatchObservable
import mort.ar.searxme.access.SearxDatabase
import mort.ar.searxme.manager.SearchManager
import mort.ar.searxme.manager.SearchParameterManager
import mort.ar.searxme.manager.SearchParams
import mort.ar.searxme.manager.SearxInstanceManager
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
    fun provideSearchManager(app: Application) =
        SearchManager(
            provideSearchParameterManager(app),
            provideSearxInstanceManager(app)
        )

    @Singleton
    @Provides
    fun provideSearchParameterManager(app: Application) =
        SearchParameterManager(provideSearchParameterSharedPreferences(app))

    @Singleton
    @Provides
    fun provideSearxInstanceManager(app: Application) =
        SearxInstanceManager(provideSearxInstanceDao(provideDatabase(app)))

    @Singleton
    @Provides
    fun provideSearchBoxTextWatcher() =
        TextWatchObservable()

    @Singleton
    @Provides
    fun provideSearchParams(app: Application) =
        SearchParams(provideSearchParameterSharedPreferences(app))

}
