package mort.ar.searxme

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
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

        replaceFragment(mActiveFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView

        val searchBoxHandler = getSearchBoxHandler(searchView)
        searchView.setOnQueryTextListener(searchBoxHandler)

        mCompositeDisposable += searchBoxHandler.mQueryTextSubmitObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { executeSearch(it) }

        setSearchViewStyle(searchView)

        val home = menu.findItem(R.id.action_home)
        home?.setOnMenuItemClickListener { goHome(searchView) }

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Initiates the styling of toolbars [searchView]
     */
    private fun setSearchViewStyle(searchView: SearchView) {
        searchView.setIconifiedByDefault(false)
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
        searchEditText.setTextAppearance(R.style.Searchbox)

        searchView.clearFocus()
    }

    /**
     * Initiates the items [ArrayAdapter] for search suggestions and returns a [SearchBoxHandler] instance
     */
    private fun getSearchBoxHandler(searchView: SearchView): SearchBoxHandler {
        val itemsAdapter = ArrayAdapter<String>(
            this,
            R.layout.search_suggestions_box,
            mutableListOf<String>()
        )

        return SearchBoxHandler(mSearchManager, searchView, itemsAdapter, listView)
    }

    /**
     * Executes the search with the provided [searchTerm].
     */
    private fun executeSearch(searchTerm: String) {
        mActiveFragment.view?.hideKeyboard()
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

    /**
     * Goes back to the [StartFragment] and clears the [searchView].
     */
    private fun goHome(searchView: SearchView): Boolean {
        mActiveFragment.view?.hideKeyboard()
        replaceFragment(mStartFragment)
        with(searchView) {
            setQuery("", false)
            clearFocus()
        }
        return true
    }

    /**
     * Starts a new [WebViewFragment] with the provided [item].url
     */
    override fun onSearchResultListItemClick(item: SearxResult?) {
        if (item != null) replaceFragment(WebViewFragment.newInstance(item.url))
    }

    override fun onWebViewFragmentInteraction() {}

    /**
     * Replaces the [mActiveFragment] with the provided [fragment]
     * and sets [mBackPressForQuit] to false again
     */
    private fun replaceFragment(fragment: Fragment) {
        mActiveFragment = fragment
        mBackPressForQuit = false
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
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
 * Class that handles interactions of the [SearchView] and acts as a pimped [SearchView.OnQueryTextListener].
 */
class SearchBoxHandler(
    private val mSearchManager: SearchManager,
    private val mSearchView: SearchView,
    private val mItemsAdapter: ArrayAdapter<String>,
    mListView: ListView
) : SearchView.OnQueryTextListener {

    private var isListSubmit = false

    private lateinit var mQueryTextSubmitEmitter: ObservableEmitter<String>
    val mQueryTextSubmitObservable: Observable<String>

    init {
        mQueryTextSubmitObservable = Observable.create { mQueryTextSubmitEmitter = it }
        mListView.adapter = mItemsAdapter
        mListView.setOnItemClickListener { adapterView, _, position, _ ->
            val query = adapterView.getItemAtPosition(position) as String
            isListSubmit = true
            mSearchView.setQuery(query, false)
            submitQuery(query)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = submitQuery(query)

    override fun onQueryTextChange(query: String?): Boolean {
        when {
            isListSubmit -> isListSubmit = !isListSubmit

            else -> when {
                query.isNullOrEmpty() -> {
                    mItemsAdapter.clear()
                    mItemsAdapter.notifyDataSetChanged()
                }
                else -> mSearchManager.getSearchAutoComplete(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { suggestions ->
                        mItemsAdapter.clear()
                        suggestions.forEach { mItemsAdapter.add(it) }
                        mItemsAdapter.notifyDataSetChanged()
                    }
            }
        }

        return false
    }

    private fun submitQuery(query: String?): Boolean {
        mItemsAdapter.clear()
        mItemsAdapter.notifyDataSetChanged()
        query?.let { mQueryTextSubmitEmitter.onNext(it) }

        return false
    }

}

/**
 * Extension function to [View] to hide the keyboard.
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
