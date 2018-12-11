package mort.ar.searxme.injection

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import mort.ar.searxme.access.SearxDatabase
import mort.ar.searxme.access.SearxInstanceDao
import mort.ar.searxme.manager.SearchManager
import mort.ar.searxme.manager.SearchParameterManager
import mort.ar.searxme.manager.SearxInstanceManager
import javax.inject.Singleton


@Module
internal class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(app: Application): SearxDatabase {
        return Room
            .databaseBuilder(
                app,
                SearxDatabase::class.java,
                "searx.db"
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideSearchManager(app: Application): SearchManager {
        return SearchManager(
            app,
            provideSearchParameterManager(app),
            provideSearxInstanceManager(app)
        )
    }

    @Singleton
    @Provides
    fun provideSearchParameterManager(app: Application): SearchParameterManager {
        return SearchParameterManager(app)
    }

    @Singleton
    @Provides
    fun provideSearxInstanceManager(app: Application): SearxInstanceManager {
        return SearxInstanceManager(provideSearxInstanceDao(provideDatabase(app)))
    }

    @Singleton
    @Provides
    fun provideSearxInstanceDao(database: SearxDatabase): SearxInstanceDao {
        return database.searxInstanceDao()
    }

}
