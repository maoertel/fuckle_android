package mort.ar.searxme.manager

import io.reactivex.Observable
import mort.ar.searxme.access.SearxInstanceDao
import mort.ar.searxme.model.SearxInstance
import javax.inject.Inject

private val initialInstance =
    SearxInstance(name = "https://searx.0x1b.de/", url = "https://searx.0x1b.de/", favorite = true)


class SearxInstanceBucket @Inject constructor(private val mInstanceDao: SearxInstanceDao) {

    private val mInstances = ArrayList<SearxInstance>()

    fun getFirstInstance(): Observable<SearxInstance> {
        return when {
            mInstances.isNotEmpty() -> Observable.just(mInstances.first())
            else -> mInstanceDao.getAllSearxInstances()
                .toObservable()
                .flatMap {
                    when {
                        it.isNotEmpty() -> Observable.just(it.first())
                        else -> {
                            mInstanceDao.insert(initialInstance)
                            mInstances += initialInstance
                            Observable.just(initialInstance)
                        }
                    }
                }
        }
    }

}
