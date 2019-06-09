package mort.ar.searxme.data.model

data class SearchResult(
    val title: String,
    val url: String,
    val prettyUrl: String,
    val content: String,
    val engines: List<String>
)