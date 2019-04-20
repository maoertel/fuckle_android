package mort.ar.searxme.di

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
internal class AppModule {

    @Provides
    fun provideCompositeDisposables() = CompositeDisposable()

}