package mort.ar.searxme.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.suggestions_list_entry.view.*
import mort.ar.searxme.R
import javax.inject.Inject
import kotlin.properties.Delegates

class SearchSuggestionsAdapter @Inject constructor(
    private val searchPresenter: SearchContract.SearchSuggestionPresenter
) : RecyclerView.Adapter<SearchSuggestionsAdapter.SearchSuggestionViewHolder>() {

    internal var searchSuggestions by Delegates.observable(emptyList<String>()) { _, _, _ -> notifyDataSetChanged() }

    private val onClickListener: View.OnClickListener by lazy {
        View.OnClickListener { view -> searchPresenter.onSearchSuggestionClick(view.tag as String) }
    }

    inner class SearchSuggestionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val suggestionTitle: TextView = view.suggestionTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.suggestions_list_entry, parent, false)
            .let { SearchSuggestionViewHolder(it) }

    override fun onBindViewHolder(holder: SearchSuggestionViewHolder, position: Int) {
        val item = searchSuggestions[position]
        holder.suggestionTitle.text = item

        with(holder.view) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = searchSuggestions.size

}