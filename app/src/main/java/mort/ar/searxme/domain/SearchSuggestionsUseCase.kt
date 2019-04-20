package mort.ar.searxme.domain

import io.reactivex.Single

interface SearchSuggestionsUseCase {

    fun requestSearchAutoComplete(query: String): Single<List<String>>

}