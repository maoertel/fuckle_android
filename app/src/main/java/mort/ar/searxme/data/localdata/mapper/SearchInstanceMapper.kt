package mort.ar.searxme.data.localdata.mapper

import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.data.model.SearchInstance

class SearchInstanceMapper {

    fun mapFromSearxInstanceEntity(entity: SearxInstanceEntity) =
        SearchInstance(
            name = entity.name,
            url = entity.url,
            favorite = entity.favorite
        )

    fun mapToSearxInstanceEntity(instance: SearchInstance) =
        SearxInstanceEntity(
            name = instance.name,
            url = instance.url,
            favorite = instance.favorite
        )

}