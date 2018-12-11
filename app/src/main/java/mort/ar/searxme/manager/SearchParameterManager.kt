package mort.ar.searxme.manager

import android.content.SharedPreferences
import io.reactivex.Observable
import javax.inject.Inject


class SearchParameterManager @Inject constructor(private val mSharedSearchPreferences: SharedPreferences) {

    private val mSharedPreferencesObservable: Observable<String> =
        Observable.create { emitter ->
            mSharedSearchPreferences
                .registerOnSharedPreferenceChangeListener { _, key -> emitter.onNext(key) }
        }

    val mSearchParams: SearchParams = SearchParams(mSharedSearchPreferences)
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

    var language: String? = sharedPreferences.getString("language", null)
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("language", value)
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

    var timeRange: String? = sharedPreferences.getString("time_range", null)
        set(value) {
            field = value
            sharedPreferences
                .edit()
                .putString("time_range", value)
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
