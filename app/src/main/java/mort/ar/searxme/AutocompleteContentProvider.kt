package mort.ar.searxme

import android.app.SearchManager
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.BaseColumns._ID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * ContentProvider for the search suggests
 */
class AutocompleteContentProvider : ContentProvider() {

    @Inject
    lateinit var mSearchManager: mort.ar.searxme.manager.SearchManager

    private var mSearchResults: MatrixCursor? = null
    private var mSearchString: String? = null

    private val mCompositeDisposable = CompositeDisposable()

    private val searchResultsCursor: MatrixCursor
        get() {
            mSearchResults = MatrixCursor(matrixCursorColumns)
            if (!mSearchString.isNullOrEmpty()) {
                mCompositeDisposable += mSearchManager.getSearchAutoComplete(mSearchString!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe { searchSuggestions ->
                        val mRow = arrayOfNulls<Any>(3)
                        var counterId = 0
                        for (suggest in searchSuggestions) {
                            if (suggest.toLowerCase().contains(mSearchString!!)) {
                                mRow[0] = Integer.toString(counterId++)
                                mRow[1] = suggest
                                mRow[2] = suggest

                                mSearchResults!!.addRow(mRow)
                            }
                        }
                    }
            }

            return mSearchResults as MatrixCursor
        }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        mSearchString = when {
            selectionArgs != null
                    && selectionArgs.isNotEmpty()
                    && selectionArgs[0].isNotEmpty() -> selectionArgs[0]
            else -> ""
        }

        return searchResultsCursor
    }

    companion object {
        private val matrixCursorColumns = arrayOf(
            // the ascending id of the search suggests
            _ID,
            // the text that appears per row in the search suggests drop down
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            // the term for the actual query
            SearchManager.SUGGEST_COLUMN_INTENT_DATA
        )
    }

    override fun onCreate(): Boolean = true

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? = null

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int = 0

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int = 0

}
