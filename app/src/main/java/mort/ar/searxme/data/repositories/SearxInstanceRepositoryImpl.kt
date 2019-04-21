package mort.ar.searxme.data.repositories

import io.reactivex.Single
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.localdata.SearxInstanceDataSource
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import javax.inject.Inject

class SearxInstanceRepositoryImpl @Inject constructor(
    private val searxInstanceDataSource: SearxInstanceDataSource
) : SearxInstanceRepository {

    override fun observeAllInstances() = searxInstanceDataSource.observeAllInstances()

    override fun observePrimaryInstance() = searxInstanceDataSource.observePrimaryInstance()

    override fun setPrimaryInstance(instance: String) = searxInstanceDataSource.setPrimaryInstance(instance)

    override fun getAllInstances(): Single<List<SearxInstanceEntity>> = searxInstanceDataSource.getAllInstances()

}