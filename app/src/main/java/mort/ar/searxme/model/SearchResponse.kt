package mort.ar.searxme.model

import com.squareup.moshi.Json
import java.io.Serializable


data class SearxResponse(
    @field:Json(name = "number_of_results") val numberOfResults: Long,
    @field:Json(name = "corrections") val corrections: Array<String>,
    @field:Json(name = "query") val query: String,
    @field:Json(name = "infoboxes") val infoBoxes: Array<InfoBox>,
    @field:Json(name = "suggestions") val suggestions: Array<String>,
    @field:Json(name = "results") val results: Array<SearxResult>,
    @field:Json(name = "answers") val answers: Array<String>,
    @field:Json(name = "unresponsive_engines") val unresponsiveEngines: Array<Array<String>>
) : Serializable

data class SearxResult(
    @field:Json(name = "engine") val engine: String,
    @field:Json(name = "category") val category: String,
    @field:Json(name = "engines") val engines: Array<String>,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "url") val url: String,
    @field:Json(name = "positions") val positions: Array<Int>,
    @field:Json(name = "parsed_url") val parsed_url: Array<String>,
    @field:Json(name = "content") val content: String,
    @field:Json(name = "pretty_url") val prettyUrl: String,
    @field:Json(name = "score") val score: Double
) : Serializable

data class InfoBox(
    @field:Json(name = "engine") val engine: String,
    @field:Json(name = "content") val content: String,
    @field:Json(name = "infobox") val infobox: String,
    @field:Json(name = "img_src") val img_src: String,
    @field:Json(name = "urls") val urls: Array<SearxUrl>,
    @field:Json(name = "attributes") val attributes: Array<Map<String, String>>,
    @field:Json(name = "id") val id: String
) : Serializable

data class SearxUrl(
    @field:Json(name = "url") val url: String,
    @field:Json(name = "title") val title: String
) : Serializable
