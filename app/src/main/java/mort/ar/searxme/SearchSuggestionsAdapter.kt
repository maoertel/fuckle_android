package mort.ar.searxme

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.ObservableEmitter
import kotlinx.android.synthetic.main.suggestions_list_entry.view.*
import mort.ar.searxme.SearchResultFragment.OnSearchResultFragmentInteractionListener


/**
 * [RecyclerView.Adapter] that can display a list of search suggestions and makes a call to the
 * specified [OnSearchResultFragmentInteractionListener].
 */
class SearchSuggestionsAdapter(
    private val mObservableEmitter: ObservableEmitter<String>
) : RecyclerView.Adapter<SearchSuggestionsAdapter.SearchSuggestionViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    var mSearchSuggestions = arrayOf<String>()

    init {
        mOnClickListener = View.OnClickListener { view ->
            mObservableEmitter.onNext(view.tag as String)
        }
    }

    inner class SearchSuggestionViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val suggestionTitle: TextView = mView.suggestionTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchSuggestionViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.suggestions_list_entry, parent, false)
        )

    override fun onBindViewHolder(holder: SearchSuggestionViewHolder, position: Int) {
        val item = mSearchSuggestions[position]
        holder.suggestionTitle.text = item

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount() = mSearchSuggestions.size

}
