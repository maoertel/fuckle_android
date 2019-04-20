package mort.ar.searxme.data.repositories

import io.reactivex.Completable
import io.reactivex.Single
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.localdata.SearchParameterDataSource
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges
import javax.inject.Inject

class SearchParameterRepositoryImpl @Inject constructor(
    private val searchParameterDataSource: SearchParameterDataSource
) : SearchParameterRepository {

    override fun getCategories(): Single<String> =
        searchParameterDataSource.getCategories()

    override fun setCategories(category: String): Completable =
        searchParameterDataSource.setCategories(category)

    override fun getEngines(): Single<String> =
        searchParameterDataSource.getEngines()

    override fun setEngines(engines: String): Completable =
        searchParameterDataSource.setEngines(engines)

    override fun getLanguage(): Single<Languages> =
        searchParameterDataSource.getLanguage()

    override fun setLanguage(language: Languages): Completable =
        searchParameterDataSource.setLanguage(language)

    override fun getTimeRange(): Single<TimeRanges> =
        searchParameterDataSource.getTimeRange()

    override fun setTimeRange(timeRange: TimeRanges): Completable =
        searchParameterDataSource.setTimeRange(timeRange)

    override fun getPageNo(): Single<Int> =
        searchParameterDataSource.getPageNo()

    override fun setPageNo(pageNumber: Int): Completable =
        searchParameterDataSource.setPageNo(pageNumber)

    override fun getFormat(): Single<String> =
        searchParameterDataSource.getFormat()

    override fun setFormat(format: String): Completable =
        searchParameterDataSource.setFormat(format)

    override fun getImageProxy(): Single<String> =
        searchParameterDataSource.getImageProxy()

    override fun setImageProxy(imageProxy: String): Completable =
        searchParameterDataSource.setImageProxy(imageProxy)

    override fun getAutoComplete(): Single<String> =
        searchParameterDataSource.getAutoComplete()

    override fun setAutocomplete(autoComplete: String): Completable =
        searchParameterDataSource.setAutocomplete(autoComplete)

    override fun getSafeSearch(): Single<String> =
        searchParameterDataSource.getSafeSearch()

    override fun setSafeSearch(safeSearch: String): Completable =
        searchParameterDataSource.setSafeSearch(safeSearch)

}