package mort.ar.searxme.data.repositories

import io.reactivex.Completable
import io.reactivex.Single
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.localdata.SearxInstanceDataSource
import mort.ar.searxme.data.model.SearchInstance
import javax.inject.Inject

class SearxInstanceRepositoryImpl @Inject constructor(
    private val searxInstanceDataSource: SearxInstanceDataSource
) : SearxInstanceRepository {

    override fun setPrimaryInstance(instance: String) = searxInstanceDataSource.setPrimaryInstance(instance)

    override fun getAllInstances(): Single<List<SearchInstance>> = searxInstanceDataSource.getAllInstances()

    override fun insert(instance: SearchInstance): Completable = searxInstanceDataSource.insert(instance)

}