package mort.ar.searxme.manager

import android.content.Context
import io.reactivex.Observable
import mort.ar.searxme.access.SearxDatabase
import mort.ar.searxme.model.SearxInstance

private val initialInstance = SearxInstance(name = "https://anonyk.com/", url = "https://anonyk.com/", favorite = true)


class SearxInstanceManager(context: Context) {

    private val mDatabase: SearxDatabase = SearxDatabase.getInstance(context)!!
    private val mInstanceDao = mDatabase.searxInstanceDao()
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
