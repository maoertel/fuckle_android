package mort.ar.searxme.data.localdata.localdatasources

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.data.localdata.SearxInstanceDataSource
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.database.daos.SearxInstanceDao
import javax.inject.Inject

// TODO Following two instances are just for test reasons. Later on their will be an edit mode to add, edit and delete instances
private val initialInstance =
    SearxInstanceEntity(
        name = "https://searx.0x1b.de/",
        url = "https://searx.0x1b.de/",
        favorite = false
    )

private val secondaryInstance =
    SearxInstanceEntity(
        name = "https://anonyk.com/",
        url = "https://anonyk.com/",
        favorite = true
    )

class SearxInstanceDataSourceImpl @Inject constructor(
    private val instanceDao: SearxInstanceDao
) : SearxInstanceDataSource {

    init {
        CompositeDisposable().apply {
            Completable
                .merge(listOf(instanceDao.insert(initialInstance), instanceDao.insert(secondaryInstance)))
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onComplete = { this.dispose() },
                    onError = { this.dispose() }
                )
                .addTo(this)
        }
    }

    override fun observeAllInstances(): Observable<List<SearxInstanceEntity>> =
        instanceDao.observeAllSearxInstances()

    override fun getAllInstances(): Single<List<SearxInstanceEntity>> =
        instanceDao.getAllSearxInstances()

    override fun observePrimaryInstance(): Observable<SearxInstanceEntity> =
        instanceDao.observeFavoriteInstance()

    override fun setPrimaryInstance(instance: String): Completable =
        instanceDao.changeFavoriteInstance(instance)

    fun getPrimaryInstance(): Single<SearxInstanceEntity> =
        instanceDao.getFavoriteInstance()

}