package mort.ar.searxme.injection

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mort.ar.searxme.MainActivity


@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [ActivityMainModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

}

@Module
internal class ActivityMainModule // not in use yet
