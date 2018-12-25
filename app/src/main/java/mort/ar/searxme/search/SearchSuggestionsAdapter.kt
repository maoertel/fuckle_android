package mort.ar.searxme.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.suggestions_list_entry.view.*
import mort.ar.searxme.R
import javax.inject.Inject


class SearchSuggestionsAdapter @Inject constructor(
    private val searchPresenter: SearchContract.SearchPresenter
//    private val searchPresenter: SearchContract.SearchSuggestionPresenter
) : RecyclerView.Adapter<SearchSuggestionsAdapter.SearchSuggestionViewHolder>() {

    private val onClickListener: View.OnClickListener
    private val emptySuggestions = listOf<String>()
    private var searchSuggestions = emptySuggestions

    init {
        onClickListener = View.OnClickListener { view ->
            searchPresenter.onSearchSuggestionClick(view.tag as String)
        }
    }

    inner class SearchSuggestionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val suggestionTitle: TextView = view.suggestionTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchSuggestionViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.suggestions_list_entry, parent, false)
        )

    override fun onBindViewHolder(holder: SearchSuggestionViewHolder, position: Int) {
        val item = searchSuggestions[position]
        holder.suggestionTitle.text = item

        with(holder.view) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    fun updateSuggestions(searchSuggestions: List<String>) {
        this.searchSuggestions = searchSuggestions
        notifyDataSetChanged()
    }

    fun clearSuggestions() {
        this.searchSuggestions = emptySuggestions
        notifyDataSetChanged()
    }

    override fun getItemCount() = searchSuggestions.size

}
