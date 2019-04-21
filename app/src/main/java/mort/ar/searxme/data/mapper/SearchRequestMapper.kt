package mort.ar.searxme.data.mapper

import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges

class SearchRequestMapper(
    private val query: String,
    private val categories: String?,
    private val engines: String?,
    private val language: Languages?,
    private val pageNo: Int?,
    private val timeRange: TimeRanges?,
    private val format: String?,
    private val imageProxy: String?,
    private val autoComplete: String?,
    private val safeSearch: String?
) {

    fun mapToSearchRequest(): SearchRequest =
        SearchRequest(
            query = query,
            categories = categories?.ifEmptyReturnNull(),
            engines = engines?.ifEmptyReturnNull(),
            language = language?.languageParameter?.ifEmptyReturnNull(),
            pageNo = pageNo,
            timeRange = timeRange?.rangeParameter?.ifEmptyReturnNull(),
            format = format?.ifEmptyReturnNull(),
            imageProxy = imageProxy?.ifEmptyReturnNull(),
            autoComplete = autoComplete?.ifEmptyReturnNull(),
            safeSearch = safeSearch?.ifEmptyReturnNull()
        )

    private fun String.ifEmptyReturnNull() = if (this.isEmpty()) null else this

}