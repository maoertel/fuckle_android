package mort.ar.searxme.data.repositories

import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.localdata.SearxInstanceDataSource
import javax.inject.Inject

class SearxInstanceRepositoryImpl @Inject constructor(
    private val searxInstanceDataSource: SearxInstanceDataSource
) : SearxInstanceRepository {

    override fun observeAllInstances() = searxInstanceDataSource.observeAllInstances()

    override fun observePrimaryInstance() = searxInstanceDataSource.observePrimaryInstance()

    override fun setPrimaryInstance(instance: String) = searxInstanceDataSource.setPrimaryInstance(instance)

}