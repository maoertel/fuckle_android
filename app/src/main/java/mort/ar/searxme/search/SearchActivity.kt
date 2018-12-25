package mort.ar.searxme.search

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_web_view.*
import mort.ar.searxme.R
import mort.ar.searxme.WebViewFragment
import mort.ar.searxme.model.SearxResponse
import javax.inject.Inject


class SearchActivity : AppCompatActivity(), SearchContract.SearchView {

    @Inject
    lateinit var searchPresenter: SearchContract.SearchPresenter

    @Inject
    lateinit var searchSuggestionsAdapter: SearchSuggestionsAdapter

    @Inject
    lateinit var searchResultAdapter: SearchResultAdapter

    @Inject
    lateinit var suggestionsLinearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var searchResultLinearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var webViewFragment: WebViewFragment

    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        with(searchSuggestionsList) {
            layoutManager = suggestionsLinearLayoutManager
            adapter = searchSuggestionsAdapter
        }

        with(searchResultList) {
            layoutManager = searchResultLinearLayoutManager
            adapter = searchResultAdapter
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.webViewFragmentContainer, webViewFragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val search = menu?.findItem(R.id.action_search)
        searchView = search?.actionView as SearchView
        initSearchView(searchView)

        val home = menu.findItem(R.id.action_home)
        home?.setOnMenuItemClickListener { searchPresenter.onHomeButtonClick() }

        return super.onCreateOptionsMenu(menu)
    }

    private fun initSearchView(searchView: SearchView) {
        searchView.setIconifiedByDefault(false)
        searchView.isSubmitButtonEnabled = false
        searchView.queryHint = getString(R.string.activity_search_searchbox_hint)

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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean =
                searchPresenter.onQueryTextSubmit(query)

            override fun onQueryTextChange(query: String?): Boolean =
                searchPresenter.onQueryTextChange(query)
        })

        searchView.clearFocus()
    }

    override fun setSearchQuery(query: String) {
        with(searchView) {
            setQuery(query, false)
            clearFocus()
        }
    }

    override fun updateSearchResults(response: SearxResponse) {
        searchResultAdapter.updateSearchResults(response)
    }

    override fun showSearchResults() {
        searchResultList.visibility = View.VISIBLE
    }

    override fun hideSearchResults() {
        searchResultList.visibility = View.GONE
    }

    override fun updateSearchSuggestions(searchSuggestions: List<String>) {
        searchSuggestionsAdapter.updateSuggestions(searchSuggestions)
    }

    override fun hideSearchSuggestions() {
        searchSuggestionsAdapter.clearSuggestions()
    }

    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    override fun showWebView() {
        webViewFragmentContainer.visibility = View.VISIBLE
    }

    override fun hideWebView() {
        webViewFragmentContainer.visibility = View.INVISIBLE
    }

    override fun webViewOnBackPressed() = webViewFragment.onBackPressed()

    override fun showProgress() {
        indeterminateBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        indeterminateBar.visibility = View.GONE
    }

    override fun showMessage(message: String?) {
        Toast
            .makeText(this, message, Toast.LENGTH_LONG)
            .show()
    }

    override fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(toolbar.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.stop()
    }

    override fun onBackPressed() {
        if (!searchPresenter.onBackPressed()) super.onBackPressed()
    }
}
