package mort.ar.searxme

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_searchresult_entry.view.*
import mort.ar.searxme.SearchResultFragment.OnSearchResultFragmentInteractionListener
import mort.ar.searxme.model.SearxResponse
import mort.ar.searxme.model.SearxResult


/**
 * [RecyclerView.Adapter] that can display a list of [SearxResult] and makes a call to the
 * specified [OnSearchResultFragmentInteractionListener].
 */
class SearchResultAdapter(
    private val mSearxResponse: SearxResponse,
    private val mListener: OnSearchResultFragmentInteractionListener?
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { view ->
            mListener?.onListItemClick(if (view.tag != null) view.tag as SearxResult else null)
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val text: TextView = mView.resultTitle
        val description: TextView = mView.resultDescription
        val url: TextView = mView.resultUrl
        val iconGoogle: ImageView = mView.iconGoogle
        val iconDuckDuckGo: ImageView = mView.iconDuckDuckGo
        val iconBing: ImageView = mView.iconBing
        val iconWikipedia: ImageView = mView.iconWikipedia
        val iconAmazon: ImageView = mView.iconAmazon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_searchresult_entry, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mSearxResponse.results[position]
        holder.text.text = item.title
        holder.description.text = item.content
        holder.url.text = item.url

        if (item.engines.contains("google")) holder.iconGoogle.visibility = View.VISIBLE
        if (item.engines.contains("duckduckgo")) holder.iconDuckDuckGo.visibility = View.VISIBLE
        if (item.engines.contains("bing")) holder.iconBing.visibility = View.VISIBLE
        if (item.engines.contains("wikipedia")) holder.iconWikipedia.visibility = View.VISIBLE
        if (item.engines.contains("amazon")) holder.iconAmazon.visibility = View.VISIBLE

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount() = mSearxResponse.results.size

}
