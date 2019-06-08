package mort.ar.searxme.domain.mapper

import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.SettingsParameter
import mort.ar.searxme.presentation.model.TimeRanges
import mort.ar.searxme.presentation.settings.Categories
import mort.ar.searxme.presentation.settings.Engines
import javax.inject.Inject

class SettingsParameterMapper @Inject constructor() {

    fun mapToSettingsParameter(
        searxInstances: List<SearxInstanceEntity>,
        engines: String,
        categories: String,
        language: Languages,
        timeRange: TimeRanges
    ) = SettingsParameter(
        searxInstances = searxInstances.map { it.url },
        engines = engines.getListOf { engine -> Engines.valueOf(engine.toUpperCase()) },
        categories = categories.getListOf { category -> Categories.valueOf(category.toUpperCase()) },
        language = language,
        timeRange = timeRange
    )

    private fun <T> String.getListOf(transformer: (String) -> T): List<T> =
        this.split(",")
            .map { it.trim() }
            .filterNot { it.isBlank() }
            .map { transformer(it) }

}