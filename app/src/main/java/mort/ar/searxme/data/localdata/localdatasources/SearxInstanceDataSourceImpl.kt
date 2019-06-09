package mort.ar.searxme.data.localdata.localdatasources

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.data.DataModule
import mort.ar.searxme.data.localdata.SearxInstanceDataSource
import mort.ar.searxme.data.localdata.mapper.SearchInstanceMapper
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.data.model.SearchInstance
import mort.ar.searxme.database.daos.SearxInstanceDao
import javax.inject.Inject

class SearxInstanceDataSourceImpl @Inject constructor(
    private val instanceDao: SearxInstanceDao,
    private val searchInstanceMapper: SearchInstanceMapper
) : SearxInstanceDataSource {

    override fun getAllInstances(): Single<List<SearchInstance>> =
        instanceDao.getAllSearxInstances()
            .map { list -> list.map { searchInstanceMapper.mapFromSearxInstanceEntity(it) } }
            .flatMap { list -> Single.just(list.sortedByDescending { it.favorite }) }

    override fun setPrimaryInstance(instance: String): Completable =
        Completable.create {
            (instanceDao.changeFavoriteInstanceSync(instance))
            it.onComplete()
        }

    init {
        // TODO following stuff is just a temporary workaround until until it is possible to add, edit, delete instances
        CompositeDisposable().apply {
            Completable.merge(instanceList.map { instanceDao.insert(it) })
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onComplete = {
                        DataModule.currentHost = instanceList.filter { it.favorite }.first().url
                        this.dispose()
                    },
                    onError = { this.dispose() }
                )
                .addTo(this)
        }
    }

}

private val instanceList =
    listOf(
        SearxInstanceEntity(
            name = "https://searx.0x1b.de",
            url = "https://searx.0x1b.de",
            favorite = false
        ),
        SearxInstanceEntity(
            name = "https://searx.be",
            url = "https://searx.be",
            favorite = false
        ),
        SearxInstanceEntity(
            name = "https://anonyk.com",
            url = "https://anonyk.com",
            favorite = false
        ),
        SearxInstanceEntity(
            name = "https://search.gibberfish.org",
            url = "https://search.gibberfish.org",
            favorite = true
        )
    )