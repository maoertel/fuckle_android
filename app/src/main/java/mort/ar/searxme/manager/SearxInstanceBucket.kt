package mort.ar.searxme.manager

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.access.SearxInstanceDao
import mort.ar.searxme.model.SearxInstance
import javax.inject.Inject


private val initialInstance =
    SearxInstance(
        name = "https://searx.0x1b.de/",
        url = "https://searx.0x1b.de/",
        favorite = true
    )


class SearxInstanceBucket @Inject constructor(
    private val instanceDao: SearxInstanceDao
) {

    init {
        insertInitialInstance()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun getAllInstances(): Observable<List<SearxInstance>> =
        instanceDao.getAllSearxInstances().toObservable()


    fun getPrimaryInstance(): Observable<SearxInstance> =
        instanceDao.getFavoriteInstance().toObservable()

    fun setPrimaryInstance(instance: String) {
        // TODO
    }

    private fun insertInitialInstance() =
        Completable.create {
            instanceDao.insert(initialInstance)
            it.onComplete()
        }

}
