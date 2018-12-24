package mort.ar.searxme.injection

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import mort.ar.searxme.search.SearchActivity


@Module
abstract class ActivityBuilder {

    @Binds
    @IntoMap
    @ActivityKey(SearchActivity::class)
    internal abstract fun bindDetailActivity(builder: SearchActivityComponent.Builder): AndroidInjector.Factory<out Activity>

}