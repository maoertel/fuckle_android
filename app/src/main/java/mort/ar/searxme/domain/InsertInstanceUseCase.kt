package mort.ar.searxme.domain

import io.reactivex.Completable
import mort.ar.searxme.data.model.SearchInstance

interface InsertInstanceUseCase {

    fun insert(instance: SearchInstance): Completable

}