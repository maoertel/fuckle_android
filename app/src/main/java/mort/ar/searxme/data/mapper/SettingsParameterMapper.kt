package mort.ar.searxme.data.mapper

import mort.ar.searxme.data.model.SearchInstance
import mort.ar.searxme.data.model.SettingsParameter
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges
import mort.ar.searxme.presentation.model.Categories
import mort.ar.searxme.presentation.model.Engines
import javax.inject.Inject

class SettingsParameterMapper @Inject constructor() {

    fun mapToSettingsParameter(
        searchInstances: List<SearchInstance>,
        engines: String,
        categories: String,
        language: Languages,
        timeRange: TimeRanges
    ) = SettingsParameter(
        searxInstances = searchInstances.map { it.url },
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