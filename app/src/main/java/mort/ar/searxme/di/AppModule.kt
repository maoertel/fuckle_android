package mort.ar.searxme.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import mort.ar.searxme.database.Database
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal class AppModule {

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

    @Provides
    fun provideCompositeDisposables() =
        CompositeDisposable()

}