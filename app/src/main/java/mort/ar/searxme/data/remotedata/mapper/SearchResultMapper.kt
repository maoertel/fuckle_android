package mort.ar.searxme.data.remotedata.mapper

import mort.ar.searxme.data.model.SearchResult
import mort.ar.searxme.data.remotedata.model.SearxResult
import javax.inject.Inject

class SearchResultMapper @Inject constructor() {

  infix fun mapFromSearxResult(searxResult: SearxResult): SearchResult =
        SearchResult(
            title = searxResult.title,
            url = searxResult.url,
            prettyUrl = searxResult.prettyUrl,
            content = searxResult.content ?: "",
            engines = searxResult.engines
        )
}