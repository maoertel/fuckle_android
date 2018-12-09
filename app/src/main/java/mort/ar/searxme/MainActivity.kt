package mort.ar.searxme

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import android.widget.Toolbar
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import mort.ar.searxme.manager.SearchManager
import mort.ar.searxme.model.SearxResult


class MainActivity : AppCompatActivity(),
    SearchResultFragment.OnSearchResultFragmentInteractionListener,
    WebViewFragment.OnWebViewFragmentInteractionListener {

    private val mStartFragment = StartFragment()
    private var mSearchResultFragment: Fragment? = null
    private var mActiveFragment: Fragment = mStartFragment

    private lateinit var mSearchboxTextEmptyObservable: Observable<Boolean>
    private val mTextWatcher = object : TextWatcher {
        private lateinit var mEmitter: ObservableEmitter<Boolean>

        init {
            mSearchboxTextEmptyObservable = Observable.create { mEmitter = it }
        }

        override fun onTextChanged(searchText: CharSequence?, start: Int, before: Int, count: Int) {
            mEmitter.onNext(TextUtils.isEmpty(searchText))
        }

        override fun afterTextChanged(searchText: Editable?) {}
        override fun beforeTextChanged(searchText: CharSequence?, start: Int, count: Int, after: Int) {}
    }

    private var mBackPressForQuit = false

    private lateinit var mSearchManager: SearchManager

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSearchManager = SearchManager(application)

        setActionBar(toolbar as Toolbar)
        searchBoxToolbar.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (!TextUtils.isEmpty(searchBoxToolbar.text.toString())) {
                        doSearch(searchBoxToolbar.text.toString())
                        return@setOnEditorActionListener false
                    } else {
                        Toast.makeText(this, "Searchin' for nuthin is cool, but...", Toast.LENGTH_LONG).show()
                        return@setOnEditorActionListener true
                    }
                }
                else -> return@setOnEditorActionListener false
            }
        }
        searchBoxToolbar.addTextChangedListener(mTextWatcher)

        searchBoxClearButton.setOnClickListener { searchBoxToolbar.text.clear() }

        toolbarHome.setOnClickListener {
            replaceFragment(mStartFragment)
            searchBoxToolbar.text.clear()
            mSearchResultFragment = null
        }

        mCompositeDisposable += mSearchboxTextEmptyObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { isEmpty ->
                searchBoxClearButton.visibility = if (isEmpty) View.GONE else View.VISIBLE
            }

        replaceFragment(mActiveFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        mActiveFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onListItemClick(item: SearxResult?) {
        if (item != null) replaceFragment(WebViewFragment.newInstance(item.url))
    }

    override fun onWebViewFragmentInteraction() {}

    private fun doSearch(searchTerm: String) {
        indeterminateBar.visibility = View.VISIBLE
        mCompositeDisposable += mSearchManager.getSearchResults(searchTerm)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response ->
                    mSearchResultFragment = SearchResultFragment.newInstance(response)
                    replaceFragment(mSearchResultFragment as Fragment)
                    indeterminateBar.visibility = View.GONE
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                    indeterminateBar.visibility = View.GONE
                })
    }

    override fun onBackPressed() {
        when (mActiveFragment) {
            is StartFragment -> {
                if (!mBackPressForQuit) {
                    Toast.makeText(this, "Next click quits the app", Toast.LENGTH_LONG).show()
                    mBackPressForQuit = !mBackPressForQuit
                } else {
                    finishAndRemoveTask()
                }
            }
            is SearchResultFragment -> replaceFragment(mStartFragment)
            is WebViewFragment -> {
                if (!(mActiveFragment as WebViewFragment).onBackPressed()) {
                    mSearchResultFragment?.let { replaceFragment(it) }
                }
            }
            else -> super.onBackPressed()
        }
    }

}
