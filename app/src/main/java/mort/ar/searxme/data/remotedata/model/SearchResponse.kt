package mort.ar.searxme.data.remotedata.model

import com.squareup.moshi.Json
import java.io.Serializable

data class SearchResponse(
    @field:Json(name = "number_of_results") val numberOfResults: Long,
    @field:Json(name = "corrections") val corrections: List<String>,
    @field:Json(name = "query") val query: String,
    @field:Json(name = "infoboxes") val infoBoxes: List<InfoBox>,
    @field:Json(name = "suggestions") val suggestions: List<String>,
    @field:Json(name = "results") val results: List<SearxResult>,
    @field:Json(name = "answers") val answers: List<String>,
    @field:Json(name = "unresponsive_engines") val unresponsiveEngines: Array<Array<String>>
) : Serializable

data class SearxResult(
    @field:Json(name = "engine") val engine: String,
    @field:Json(name = "category") val category: String,
    @field:Json(name = "engines") val engines: List<String>,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "url") val url: String,
    @field:Json(name = "positions") val positions: List<Int>,
    @field:Json(name = "parsed_url") val parsed_url: List<String>,
    @field:Json(name = "content") val content: String,
    @field:Json(name = "pretty_url") val prettyUrl: String,
    @field:Json(name = "score") val score: Double
) : Serializable

data class InfoBox(
    @field:Json(name = "engine") val engine: String,
    @field:Json(name = "content") val content: String,
    @field:Json(name = "infobox") val infobox: String,
    @field:Json(name = "img_src") val img_src: String,
    @field:Json(name = "urls") val urls: List<SearxUrl>,
    @field:Json(name = "attributes") val attributes: List<Map<String, String>>,
    @field:Json(name = "id") val id: String
) : Serializable

data class SearxUrl(
    @field:Json(name = "url") val url: String,
    @field:Json(name = "title") val title: String
) : Serializable