package mort.ar.searxme.data

import io.reactivex.Completable
import io.reactivex.Single
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity

interface SearxInstanceRepository {

    fun setPrimaryInstance(instance: String): Completable

    fun getAllInstances(): Single<List<SearxInstanceEntity>>

}