package mort.ar.searxme.injection

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mort.ar.searxme.MainActivity


@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity

}
