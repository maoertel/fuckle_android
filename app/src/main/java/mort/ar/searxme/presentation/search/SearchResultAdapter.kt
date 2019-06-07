package mort.ar.searxme.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.searchresult_entry.view.*
import mort.ar.searxme.R
import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.data.remotedata.model.SearxResult
import javax.inject.Inject


class SearchResultAdapter @Inject constructor(
    private val searchResultPresenter: SearchContract.SearchResultPresenter
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private var searchResponse: SearchResponse? = null

    init {
        onClickListener = View.OnClickListener { view ->
            searchResultPresenter.onSearchResultClick(view.tag as SearxResult)
        }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.searchresult_entry, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = searchResponse?.results?.get(position)
        if (item != null) {
            holder.text.text = item.title
            holder.description.text = item.content
            holder.url.text = item.url

            if (item.engines.contains("google")) holder.iconGoogle.visibility = View.VISIBLE
            if (item.engines.contains("duckduckgo")) holder.iconDuckDuckGo.visibility = View.VISIBLE
            if (item.engines.contains("bing")) holder.iconBing.visibility = View.VISIBLE
            if (item.engines.contains("wikipedia")) holder.iconWikipedia.visibility = View.VISIBLE
            if (item.engines.contains("qwant")) holder.iconQwant.visibility = View.VISIBLE

            with(holder.view) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }
    }

    fun updateSearchResults(searchResponse: SearchResponse?) {
        this.searchResponse = searchResponse
        notifyDataSetChanged()
    }

    override fun getItemCount() = searchResponse?.results?.size ?: 0

}