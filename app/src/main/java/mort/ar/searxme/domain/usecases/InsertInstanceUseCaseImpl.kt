package mort.ar.searxme.domain.usecases

import io.reactivex.Completable
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.model.SearchInstance
import mort.ar.searxme.domain.InsertInstanceUseCase
import javax.inject.Inject

class InsertInstanceUseCaseImpl @Inject constructor(
    private val searxInstanceRepository: SearxInstanceRepository
) : InsertInstanceUseCase {

    override fun insert(instance: SearchInstance): Completable = searxInstanceRepository.insert(instance)

}