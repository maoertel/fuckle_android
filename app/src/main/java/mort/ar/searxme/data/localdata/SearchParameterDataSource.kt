package mort.ar.searxme.data.localdata

import io.reactivex.Completable
import io.reactivex.Single
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges

interface SearchParameterDataSource {

    fun getCategories(): Single<String>

    fun setCategories(category: String): Completable

    fun getEngines(): Single<String>

    fun setEngines(engines: String): Completable

    fun getLanguage(): Single<Languages>

    fun setLanguage(language: Languages): Completable

    fun getTimeRange(): Single<TimeRanges>

    fun setTimeRange(timeRange: TimeRanges): Completable

    fun getPageNo(): Single<Int>

    fun setPageNo(pageNumber: Int): Completable

    fun getFormat(): Single<String>

    fun setFormat(format: String): Completable

    fun getImageProxy(): Single<String>

    fun setImageProxy(imageProxy: String): Completable

    fun getAutoComplete(): Single<String>

    fun setAutocomplete(autoComplete: String): Completable

    fun getSafeSearch(): Single<String>

    fun setSafeSearch(safeSearch: String): Completable

}