package mort.ar.searxme.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.searchresult_entry.view.*
import mort.ar.searxme.R
import mort.ar.searxme.data.model.SearchResult
import javax.inject.Inject
import kotlin.properties.Delegates

class SearchResultAdapter @Inject constructor(
    private val searchResultPresenter: SearchContract.SearchResultPresenter
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    internal var searchResults by Delegates.observable(emptyList<SearchResult>()) { _, _, _ -> notifyDataSetChanged() }

    private val onClickListener: View.OnClickListener by lazy {
        View.OnClickListener { view -> searchResultPresenter.onSearchResultClick(view.tag as SearchResult) }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.resultTitle
        val description: TextView = view.resultDescription
        val url: TextView = view.resultUrl

        val iconGoogle: ImageView = view.iconGoogle
        val iconDuckDuckGo: ImageView = view.iconDuckDuckGo
        val iconBing: ImageView = view.iconBing
        val iconWikipedia: ImageView = view.iconWikipedia
        val iconQwant: ImageView = view.iconQwant
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.searchresult_entry, parent, false)
            .let { ViewHolder(it) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int): Unit =
        searchResults[position].let { searchResult ->
            with(holder) {
                text.text = searchResult.title
                description.text = searchResult.content
                url.text = searchResult.url
            }

            with(searchResult.engines) {
                if (contains("google")) holder.iconGoogle.visibility = View.VISIBLE
                if (contains("duckduckgo")) holder.iconDuckDuckGo.visibility = View.VISIBLE
                if (contains("bing")) holder.iconBing.visibility = View.VISIBLE
                if (contains("wikipedia")) holder.iconWikipedia.visibility = View.VISIBLE
                if (contains("qwant")) holder.iconQwant.visibility = View.VISIBLE
            }

            with(holder.view) {
                tag = searchResult
                setOnClickListener(onClickListener)
            }
        }

    override fun getItemCount() = searchResults.size

}