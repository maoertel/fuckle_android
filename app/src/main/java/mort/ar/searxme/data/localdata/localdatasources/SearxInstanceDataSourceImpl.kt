package mort.ar.searxme.data.localdata.localdatasources

import io.reactivex.Completable
import io.reactivex.Single
import mort.ar.searxme.data.localdata.SearxInstanceDataSource
import mort.ar.searxme.data.localdata.mapper.SearchInstanceMapper
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

    override fun insert(instance: SearchInstance): Completable =
        instanceDao.insert(searchInstanceMapper.mapToSearxInstanceEntity(instance))

}