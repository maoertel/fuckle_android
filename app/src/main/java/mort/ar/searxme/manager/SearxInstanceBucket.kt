package mort.ar.searxme.manager

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.access.SearxInstanceDao
import mort.ar.searxme.model.SearxInstance
import javax.inject.Inject


private val initialInstance =
    SearxInstance(
        name = "https://searx.0x1b.de/",
        url = "https://searx.0x1b.de/",
        favorite = false
    )

private val secondaryInstance =
    SearxInstance(
        name = "https://anonyk.com/",
        url = "https://anonyk.com/",
        favorite = true
    )


class SearxInstanceBucket @Inject constructor(
    private val instanceDao: SearxInstanceDao
) {

    init {
        instanceDao.insert(initialInstance).subscribeOn(Schedulers.io()).subscribe()
        instanceDao.insert(secondaryInstance).subscribeOn(Schedulers.io()).subscribe()
    }

    fun getAllInstances(): Observable<List<SearxInstance>> =
        instanceDao.getAllSearxInstances()


    fun getPrimaryInstance(): Observable<SearxInstance> =
        instanceDao.getFavoriteInstance()

    fun getPrimaryInstanceSingle(): Single<SearxInstance> =
        instanceDao.getFavoriteInstanceSingle()

    fun setPrimaryInstance(instance: String): Completable =
        Completable.concat(
            listOf(
                setInstanceToFavorite(false) { getPrimaryInstanceSingle() },
                setInstanceToFavorite(true) { instanceDao.getSearxInstance(instance) }
            )
        )

    private fun setInstanceToFavorite(
        markAsFavorite: Boolean,
        instance: () -> Single<SearxInstance>
    ): Completable =
        instance()
            .flatMap { searxInstance ->
                searxInstance.favorite = markAsFavorite
                instanceDao
                    .updateInstance(searxInstance)
                    .toSingle { }
            }
            .ignoreElement()

    /*private fun insertInitialInstance(): Completable =
        instanceDao.getAllSearxInstancesSingle()
            .flatMap { instances ->
                when (instances.isNullOrEmpty()) {
                    true ->
                        Completable
                            .merge(
                                listOf(
                                    instanceDao.insert(initialInstance),
                                    instanceDao.insert(secondaryInstance)
                                )
                            )
                            .toSingle { }
                    else -> Single.just(true)
                }
            }
            .ignoreElement()*/

}
