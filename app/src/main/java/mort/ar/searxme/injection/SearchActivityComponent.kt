package mort.ar.searxme.injection

import dagger.Subcomponent
import dagger.android.AndroidInjector
import mort.ar.searxme.search.SearchActivity

@Subcomponent(modules = [SearchActivityModule::class])
interface SearchActivityComponent : AndroidInjector<SearchActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SearchActivity>()

}