package mort.ar.searxme.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.searchresult_entry.view.*
import mort.ar.searxme.R
import mort.ar.searxme.data.remotedata.model.SearxResult
import mort.ar.searxme.presentation.model.SearchResults
import javax.inject.Inject
import kotlin.properties.Delegates

class SearchResultAdapter @Inject constructor(
    private val searchResultPresenter: SearchContract.SearchResultPresenter
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    internal var search by Delegates.observable(SearchResults(emptyList())) { _, _, _ -> notifyDataSetChanged() }

    private val onClickListener: View.OnClickListener by lazy {
        View.OnClickListener { view -> searchResultPresenter.onSearchResultClick(view.tag as SearxResult) }
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
        search.results[position].let { item ->
            with(holder) {
                text.text = item.title
                description.text = item.content
                url.text = item.url
            }

            with(item.engines) {
                if (contains("google")) holder.iconGoogle.visibility = View.VISIBLE
                if (contains("duckduckgo")) holder.iconDuckDuckGo.visibility = View.VISIBLE
                if (contains("bing")) holder.iconBing.visibility = View.VISIBLE
                if (contains("wikipedia")) holder.iconWikipedia.visibility = View.VISIBLE
                if (contains("qwant")) holder.iconQwant.visibility = View.VISIBLE
            }

            with(holder.view) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

    override fun getItemCount() = search.results.size

}