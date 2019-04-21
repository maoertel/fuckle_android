package mort.ar.searxme.domain.mapper

import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.SettingsParameter
import mort.ar.searxme.presentation.model.TimeRanges
import mort.ar.searxme.presentation.settings.Categories
import mort.ar.searxme.presentation.settings.Engines

class SettingsParameterMapper(
    private val searxInstances: List<SearxInstanceEntity>,
    private val engines: String,
    private val categories: String,
    private val language: Languages,
    private val timeRange: TimeRanges
) {

    fun mapToSettingsParameter() =
        SettingsParameter(
            searxInstances = getSortedListOfInstances(),
            engines = getListOfEngines(),
            categories = getListOfCategories(),
            language = language,
            timeRange = timeRange
        )

    private fun getSortedListOfInstances(): List<String> =
        searxInstances
            .sortedBy { it.favorite }
            .map { it.url }


    private fun getListOfEngines(): List<Engines> =
        engines
            .split(",")
            .map { it.trim() }
            .map { Engines.valueOf(it.toUpperCase()) }

    private fun getListOfCategories(): List<Categories> =
        categories
            .split(",")
            .map { it.trim() }
            .map { Categories.valueOf(it.toUpperCase()) }

}