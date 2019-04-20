package mort.ar.searxme.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.database.daos.SearxInstanceDao
import javax.inject.Inject

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

class SearxInstanceRepositoryImpl @Inject constructor(
    private val instanceDao: SearxInstanceDao
) : SearxInstanceRepository {

    init {
        instanceDao.insert(initialInstance).subscribeOn(Schedulers.io()).subscribe()
        instanceDao.insert(secondaryInstance).subscribeOn(Schedulers.io()).subscribe()
    }

    override fun observeAllInstances(): Observable<List<SearxInstanceEntity>> =
        instanceDao.observeAllSearxInstances()

    override fun observePrimaryInstance(): Observable<SearxInstanceEntity> =
        instanceDao.observeFavoriteInstance()

    override fun setPrimaryInstance(instance: String): Completable =
        instanceDao.changeFavoriteInstance(instance)

    /*fun getPrimaryInstance(): Single<SearxInstanceEntity> =
        instanceDao.getFavoriteInstance()*/

    /*Completable.concat(
        listOf(
            setInstanceToFavorite(false) { getPrimaryInstance() },
            setInstanceToFavorite(true) { instanceDao.getSearxInstance(instance) }
        )
    )*/

    /*private fun setInstanceToFavorite(
        markAsFavorite: Boolean,
        instance: () -> Single<SearxInstanceEntity>
    ): Completable =
        instance()
            .flatMap { searxInstance ->
                searxInstance.favorite = markAsFavorite
                instanceDao
                    .updateInstance(searxInstance)
                    .toSingle { }
            }
            .ignoreElement()*/

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