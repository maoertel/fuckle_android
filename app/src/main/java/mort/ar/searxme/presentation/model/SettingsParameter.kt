package mort.ar.searxme.presentation.model

import mort.ar.searxme.presentation.settings.Categories
import mort.ar.searxme.presentation.settings.Engines

data class SettingsParameter(
    val searxInstances: List<String>,
    val engines: List<Engines>,
    val categories: List<Categories>,
    val language: Languages,
    val timeRange: TimeRanges
)