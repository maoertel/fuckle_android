package mort.ar.searxme

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import mort.ar.searxme.manager.SearchManager
import mort.ar.searxme.model.SearxResult
import javax.inject.Inject


/**
 * Activity that works as the apps entry point.
 */
class MainActivity : AppCompatActivity(),
    SearchResultFragment.OnSearchResultFragmentInteractionListener,
    WebViewFragment.OnWebViewFragmentInteractionListener {

    @Inject
    lateinit var mSearchManager: SearchManager

    @Inject
    lateinit var mTextWatcher: TextWatchObservable

    private val mStartFragment = StartFragment()
    private var mSearchResultFragment: Fragment? = null
    private var mActiveFragment: Fragment = mStartFragment

    private var mBackPressForQuit = false

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        /*searchBoxToolbar.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (!TextUtils.isEmpty(searchBoxToolbar.text.toString())) {
                        doSearch(searchBoxToolbar.text.toString())
                        false
                    } else {
                        Toast.makeText(this, getString(R.string.activity_main_message_empty_search), Toast.LENGTH_LONG)
                            .show()
                        true
                    }
                }
                else -> false
            }
        }
        searchBoxToolbar.addTextChangedListener(mTextWatcher)

        searchBoxClearButton.setOnClickListener { searchBoxToolbar.text.clear() }

        toolbarHome.setOnClickListener {
            replaceFragment(mStartFragment)
            searchBoxToolbar.text.clear()
            mSearchResultFragment = null
        }

        mCompositeDisposable += mTextWatcher.mTextEmptyObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { isEmpty ->
                searchBoxClearButton.visibility = if (isEmpty) View.GONE else View.VISIBLE
            }*/

        replaceFragment(mActiveFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                doSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        setSearchViewStyle(searchView)

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Initiates the styling of toolbars search box
     */
    private fun setSearchViewStyle(searchView: SearchView) {
        searchView.setIconifiedByDefault(false)
        searchView.isIconified = false
        searchView.isSubmitButtonEnabled = false
        searchView.queryHint = getString(R.string.fragment_start_searchbox_hint)

        val searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate) as View
        searchPlate.setBackgroundColor(resources.getColor(R.color.colorPrimary))

        val searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon) as ImageView
        searchIcon.setColorFilter(getColor(R.color.toolbar_icons))
        searchIcon.layoutParams = LinearLayout.LayoutParams(0, 0)

        val closeButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn) as ImageView
        closeButton.setColorFilter(getColor(R.color.toolbar_icons))

        val submitButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_go_btn) as ImageView
        submitButton.setColorFilter(getColor(R.color.toolbar_icons))

        val submitButtonArea = searchView.findViewById(android.support.v7.appcompat.R.id.submit_area) as View
        submitButtonArea.setBackgroundColor(resources.getColor(R.color.colorPrimary))

        val searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        searchEditText.background = getDrawable(R.drawable.edittext_background)
    }

    private fun replaceFragment(fragment: Fragment) {
        mActiveFragment = fragment
        mBackPressForQuit = false
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
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

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }

}

/**
 * Class that wraps a TextWatcher listener in an Observable to observe the search box input text
 */
class TextWatchObservable : TextWatcher {

    private lateinit var mEmitter: ObservableEmitter<Boolean>
    val mTextEmptyObservable: Observable<Boolean>

    init {
        mTextEmptyObservable = Observable.create { mEmitter = it }
    }

    override fun onTextChanged(searchText: CharSequence?, start: Int, before: Int, count: Int) =
        mEmitter.onNext(TextUtils.isEmpty(searchText))

    override fun afterTextChanged(searchText: Editable?) {}
    override fun beforeTextChanged(searchText: CharSequence?, start: Int, count: Int, after: Int) {}

}
