package mort.ar.searxme.data.localdata

import io.reactivex.Completable
import io.reactivex.Observable
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity

interface SearxInstanceDataSource {

    fun observeAllInstances(): Observable<List<SearxInstanceEntity>>

    fun observePrimaryInstance(): Observable<SearxInstanceEntity>

    fun setPrimaryInstance(instance: String): Completable

}