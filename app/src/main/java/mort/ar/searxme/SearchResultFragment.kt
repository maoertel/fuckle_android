package mort.ar.searxme

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mort.ar.searxme.model.SearxResponse
import mort.ar.searxme.model.SearxResult


private const val SEARCH_RESPONSE = "search_term"


/**
 * A fragment representing a list of [SearxResult].
 * Activities containing this fragment MUST implement the
 * [SearchResultFragment.OnSearchResultFragmentInteractionListener] interface.
 */
class SearchResultFragment : Fragment() {

    private lateinit var mSearchResponse: SearxResponse
    private var mListener: OnSearchResultFragmentInteractionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { mSearchResponse = it.getSerializable(SEARCH_RESPONSE) as SearxResponse }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_searchresultentry_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = SearchResultAdapter(mSearchResponse, mListener)
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchResultFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnSearchResultFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * Interface to interact with it's implementing class
     */
    interface OnSearchResultFragmentInteractionListener {
        fun onListItemClick(item: SearxResult?)
    }

    companion object {
        /**
         * Factory method to create a new instance of
         * this fragment using the provided SearxResponse.
         *
         * @param searchResponse the SEARCH_RESPONSE to load.
         * @return A new instance of fragment WebViewFragment.
         */
        @JvmStatic
        fun newInstance(searchResponse: SearxResponse) =
            SearchResultFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SEARCH_RESPONSE, searchResponse)
                }
            }
    }

}
