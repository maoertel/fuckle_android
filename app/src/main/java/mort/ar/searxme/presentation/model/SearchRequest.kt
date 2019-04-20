package mort.ar.searxme.presentation.model

data class SearchRequest(
    val query: String,
    val categories: String? = null,
    val engines: String? = null,
    val language: String? = null,
    val pageNo: Int? = null,
    val timeRange: String? = null,
    val format: String? = null,
    val imageProxy: String? = null,
    val autoComplete: String? = null,
    val safeSearch: String? = null
)