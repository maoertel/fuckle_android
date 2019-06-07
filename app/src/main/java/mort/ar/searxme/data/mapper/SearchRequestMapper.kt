package mort.ar.searxme.data.mapper

import mort.ar.searxme.data.model.SearchRequest
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges
import javax.inject.Inject

class SearchRequestMapper @Inject constructor() {

    fun mapToSearchRequest(
        query: String,
        categories: String?,
        engines: String?,
        language: Languages?,
        pageNo: Int?,
        timeRange: TimeRanges?,
        format: String?,
        imageProxy: String?,
        autoComplete: String?,
        safeSearch: String?
    ): SearchRequest = SearchRequest(
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