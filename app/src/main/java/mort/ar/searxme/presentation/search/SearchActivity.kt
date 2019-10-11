package mort.ar.searxme.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_web_view.*
import mort.ar.searxme.R
import mort.ar.searxme.data.model.SearchResult
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

  @Inject
  lateinit var settingsIntent: Intent

  private lateinit var searchView: SearchView

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_search)
    setSupportActionBar(toolbar as Toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    searchPresenter.start()
  }

  override fun onDestroy() {
    super.onDestroy()
    searchPresenter.stop()
  }

  override fun initializeSearchResultsAdapter() =
    with(searchResultList) {
      layoutManager = searchResultLinearLayoutManager
      adapter = searchResultAdapter
    }

  override fun initializeSearchSuggestionsAdapter() =
    with(searchSuggestionsList) {
      layoutManager = suggestionsLinearLayoutManager
      adapter = searchSuggestionsAdapter
    }

  override fun initializeWebViewFragment() = replaceFragment(webViewFragment)

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.toolbar_menu, menu)

    val search = menu?.findItem(R.id.action_search)
    searchView = search?.actionView as SearchView
    initSearchView(searchView)

    val home = menu.findItem(R.id.action_home)
    home?.setOnMenuItemClickListener { searchPresenter.onHomeButtonClick() }

    val settings = menu.findItem(R.id.action_settings)
    settings.setOnMenuItemClickListener { searchPresenter.onSettingsButtonClick() }

    return super.onCreateOptionsMenu(menu)
  }

  private fun initSearchView(searchView: SearchView) {
    searchView.setIconifiedByDefault(false)
    searchView.isSubmitButtonEnabled = false
    searchView.queryHint = getString(R.string.activity_search_searchbox_hint)

    val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_plate) as View
    searchPlate.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))

    val searchIcon =
      searchView.findViewById(androidx.appcompat.R.id.search_mag_icon) as ImageView
    searchIcon.setColorFilter(getColor(R.color.activity_search_toolbar_icons))
    searchIcon.layoutParams = LinearLayout.LayoutParams(0, 0)

    val closeButton =
      searchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
    closeButton.setColorFilter(getColor(R.color.activity_search_toolbar_icons))

    val submitButton =
      searchView.findViewById(androidx.appcompat.R.id.search_go_btn) as ImageView
    submitButton.setColorFilter(getColor(R.color.activity_search_toolbar_icons))

    val submitButtonArea = searchView.findViewById(androidx.appcompat.R.id.submit_area) as View
    submitButtonArea.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))

    val searchEditText =
      searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
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

  override fun setSearchQuery(query: String) = with(searchView) {
    setQuery(query, false)
    clearFocus()
  }

  override fun updateSearchResults(searchResult: List<SearchResult>) {
    searchResultAdapter.searchResults = searchResult
  }

  override fun showSearchResults() {
    searchResultList.visibility = View.VISIBLE
  }

  override fun hideSearchResults() {
    searchResultList.visibility = View.GONE
  }

  override fun updateSearchSuggestions(searchSuggestions: List<String>) {
    searchSuggestionsAdapter.searchSuggestions = searchSuggestions
  }

  override fun hideSearchSuggestions() {
    searchSuggestionsAdapter.searchSuggestions = emptyList()
  }

  override fun loadUrl(url: String) = webView.loadUrl(url)

  override fun showWebView() {
    webViewFragmentContainer.visibility = View.VISIBLE
  }

  override fun hideWebView() {
    webViewFragmentContainer.visibility = View.INVISIBLE
  }

  override fun showProgress() {
    indeterminateBar.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    indeterminateBar.visibility = View.GONE
  }

  override fun showMessage(message: String?) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

  override fun hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(toolbar.windowToken, 0)
  }

  private fun replaceFragment(fragment: WebViewFragment) {
    supportFragmentManager
      .beginTransaction()
      .add(R.id.webViewFragmentContainer, fragment)
      .commit()
  }

  override fun startSettings() = startActivity(settingsIntent)

  override fun onBackPressedHandledByWebView() = webViewFragment.onBackPressed()

  override fun onBackPressed() =
    if (!searchPresenter.handleOnBackPress()) super.onBackPressed() else Unit

}