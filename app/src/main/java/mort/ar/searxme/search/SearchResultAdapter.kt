package mort.ar.searxme.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.searchresult_entry.view.*
import mort.ar.searxme.R
import mort.ar.searxme.model.SearxResponse
import mort.ar.searxme.model.SearxResult
import javax.inject.Inject


class SearchResultAdapter @Inject constructor(
//    private val searchResultPresenter: SearchContract.SearchResultPresenter
    private val searchResultPresenter: SearchContract.SearchPresenter
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private var searxResponse: SearxResponse? = null

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
        val iconAmazon: ImageView = view.iconAmazon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.searchresult_entry, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = searxResponse?.results?.get(position)
        if (item != null) {
            holder.text.text = item.title
            holder.description.text = item.content
            holder.url.text = item.url

            if (item.engines.contains("google")) holder.iconGoogle.visibility = View.VISIBLE
            if (item.engines.contains("duckduckgo")) holder.iconDuckDuckGo.visibility = View.VISIBLE
            if (item.engines.contains("bing")) holder.iconBing.visibility = View.VISIBLE
            if (item.engines.contains("wikipedia")) holder.iconWikipedia.visibility = View.VISIBLE
            if (item.engines.contains("amazon")) holder.iconAmazon.visibility = View.VISIBLE

            with(holder.view) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }
    }

    fun updateSearchResults(searxResponse: SearxResponse?) {
        this.searxResponse = searxResponse
        notifyDataSetChanged()
    }

    override fun getItemCount() = searxResponse?.results?.size ?: 0

}
