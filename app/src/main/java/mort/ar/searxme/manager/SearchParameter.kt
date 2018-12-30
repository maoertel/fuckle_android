package mort.ar.searxme.manager

import android.content.SharedPreferences
import io.reactivex.Observable
import mort.ar.searxme.model.Languages
import mort.ar.searxme.model.Languages.*
import mort.ar.searxme.model.TimeRanges
import mort.ar.searxme.model.TimeRanges.*
import javax.inject.Inject


class SearchParameter @Inject constructor(private val sharedSearchPreferences: SharedPreferences) {

    private val mSharedPreferencesObservable: Observable<String> =
        Observable.create { emitter ->
            sharedSearchPreferences
                .registerOnSharedPreferenceChangeListener { _, key -> emitter.onNext(key) }
        }

    val searchParams: SearchParams = SearchParams(sharedSearchPreferences)
}

class SearchParams @Inject constructor(private val sharedPreferences: SharedPreferences) {

    var categories: String? = sharedPreferences.getString("categories", null)
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("categories", value)
                .apply()
        }

    var engines: String? = sharedPreferences.getString("engines", null)
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("engines", value)
                .apply()
        }

    var language: Languages = Languages.DEFAULT
        get() =
            when (sharedPreferences.getString("language", null)) {
                "en" -> ENGLISH
                "de" -> GERMAN
                "fr" -> FRENCH
                "es" -> SPANISH
                else -> Languages.DEFAULT
            }
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("language", value.languageParameter)
                .apply()
        }

    var timeRange: TimeRanges = TimeRanges.DEFAULT
        get() =
            when (sharedPreferences.getString("time_range", null)) {
                "day" -> DAY
                "week" -> WEEK
                "month" -> MONTH
                "year" -> YEAR
                else -> TimeRanges.DEFAULT
            }
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("time_range", value.rangeParameter)
                .apply()
        }

    var pageNo: Int? = sharedPreferences.getInt("pageno", 1)
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putInt("pageno", value ?: 1)
                .apply()
        }

    var format: String? = sharedPreferences.getString("format", "json")
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("format", value)
                .apply()
        }

    var imageProxy: String? = sharedPreferences.getString("image_proxy", null)
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("image_proxy", value)
                .apply()
        }

    var autoComplete: String? = sharedPreferences.getString("autocomplete", null)
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("autocomplete", value)
                .apply()
        }

    var safeSearch: String? = sharedPreferences.getString("safesearch", null)
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("safesearch", value)
                .apply()
        }

}
