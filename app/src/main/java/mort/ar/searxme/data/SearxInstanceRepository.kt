package mort.ar.searxme.data

import io.reactivex.Completable
import io.reactivex.Observable
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity

interface SearxInstanceRepository {

    fun observeAllInstances(): Observable<List<SearxInstanceEntity>>

    fun observePrimaryInstance(): Observable<SearxInstanceEntity>

    fun setPrimaryInstance(instance: String): Completable

}