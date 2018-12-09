package mort.ar.searxme

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_web_view.*


private const val URL = "url"


/**
 * Fragment that handle the WebView interactions
 *
 * Activities must implement the [WebViewFragment.OnWebViewFragmentInteractionListener] interface
 * to handle interaction events.
 *
 * Use the [WebViewFragment.newInstance] factory method to create an instance of this fragment.
 */
class WebViewFragment : Fragment() {

    private var url: String? = null
    private var listener: OnWebViewFragmentInteractionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { url = it.getString(URL) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                view.loadUrl(request.url.toString())
                return false
            }
        }

        val webSettings = webView.settings
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.setAppCacheEnabled(false)

        webView.loadUrl(url)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnWebViewFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun onButtonPressed() {
        listener?.onWebViewFragmentInteraction()
    }

    fun onBackPressed(): Boolean {
        if (webView.canGoBack()) {
            webView.goBack()
            return true
        }

        return false
    }

    /**
     * This interface interacts with it's implementing activity
     * and potentially other fragments contained in that activity.
     */
    interface OnWebViewFragmentInteractionListener {
        fun onWebViewFragmentInteraction()
    }

    companion object {
        /**
         * Factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param url the URL to load.
         * @return A new instance of fragment WebViewFragment.
         */
        @JvmStatic
        fun newInstance(url: String) =
            WebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(URL, url)
                }
            }
    }
}
