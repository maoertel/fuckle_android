package mort.ar.searxme.data.localdata.localdatasources

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.exceptions.Exceptions
import mort.ar.searxme.data.localdata.SearchParameterDataSource
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.Languages.*
import mort.ar.searxme.presentation.model.TimeRanges
import mort.ar.searxme.presentation.model.TimeRanges.*
import javax.inject.Inject

private const val CATEGORIES = "categories"
private const val ENGINES = "engines"
private const val LANGUAGE = "language"
private const val TIME_RANGE = "time_range"
private const val PAGE_NUMBER = "pageno"
private const val FORMAT = "format"
private const val IMAGE_PROXY = "image_proxy"
private const val AUTOCOMPLETE = "autocomplete"
private const val SAFE_SEARCH = "safesearch"

class SearchParameterDataSourceImpl @Inject constructor(
  private val sharedPreferences: SharedPreferences
) : SearchParameterDataSource {

  override fun getCategories(): Single<String> = Single.just(sharedPreferences.getString(CATEGORIES, ""))

  override fun setCategories(category: String): Completable = category saveWithKey CATEGORIES

  override fun getEngines(): Single<String> = Single.just(sharedPreferences.getString(ENGINES, ""))

  override fun setEngines(engines: String): Completable = engines saveWithKey ENGINES

  override fun getLanguage(): Single<Languages> = Single.just(
    when (sharedPreferences.getString(LANGUAGE, DEFAULT_LANGUAGE.languageParameter)) {
      ENGLISH.languageParameter -> ENGLISH
      GERMAN.languageParameter -> GERMAN
      FRENCH.languageParameter -> FRENCH
      SPANISH.languageParameter -> SPANISH
      else -> DEFAULT_LANGUAGE
    }
  )

  override fun setLanguage(language: Languages): Completable = language.languageParameter saveWithKey LANGUAGE

  override fun getTimeRange(): Single<TimeRanges> = Single.just(
    when (sharedPreferences.getString(TIME_RANGE, null)) {
      DAY.rangeParameter -> DAY
      WEEK.rangeParameter -> WEEK
      MONTH.rangeParameter -> MONTH
      YEAR.rangeParameter -> YEAR
      else -> DEFAULT
    }
  )

  override fun setTimeRange(timeRange: TimeRanges): Completable = timeRange.rangeParameter saveWithKey TIME_RANGE

  override fun getPageNo(): Single<Int> = Single.just(sharedPreferences.getInt(PAGE_NUMBER, 1))

  override fun setPageNo(pageNumber: Int): Completable = pageNumber saveWithKey PAGE_NUMBER

  override fun getFormat(): Single<String> = Single.just(sharedPreferences.getString(FORMAT, "json"))

  override fun setFormat(format: String): Completable = format saveWithKey FORMAT

  override fun getImageProxy(): Single<String> = Single.just(sharedPreferences.getString(IMAGE_PROXY, ""))

  override fun setImageProxy(imageProxy: String): Completable = imageProxy saveWithKey IMAGE_PROXY

  override fun getAutoComplete(): Single<String> = Single.just(sharedPreferences.getString(AUTOCOMPLETE, ""))

  override fun setAutocomplete(autoComplete: String): Completable = autoComplete saveWithKey AUTOCOMPLETE

  override fun getSafeSearch(): Single<String> = Single.just(sharedPreferences.getString(SAFE_SEARCH, ""))

  override fun setSafeSearch(safeSearch: String): Completable = safeSearch saveWithKey SAFE_SEARCH

  private infix fun <T> T.saveWithKey(prefKey: String): Completable = Completable.fromRunnable {
    sharedPreferences.edit().let { editor ->
      when (this) {
        is String -> editor.putString(prefKey, this).apply()
        is Int -> editor.putInt(prefKey, this).apply()
        else -> Exceptions.propagate(IllegalArgumentException())
      }
    }
  }

}