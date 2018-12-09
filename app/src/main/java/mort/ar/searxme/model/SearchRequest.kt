package mort.ar.searxme.model

data class SearchRequest(

    /**
     * The search query.
     *
     * Parameter: mandatory
     */
    val query: String,

    /**
     * Comma separated list, specifies the active search categories.
     *
     * Parameter: optional
     *
     * Possible: TODO ...
     * Default: empty
     */
    val categories: String? = null,

    /**
     *  Comma separated list, specifies the active search engines.
     *
     *  Possible: google, duckduckgo, bing
     *      ->  Possibilities depend on the active engines at the
     *          specific instance of searx the search takes place)
     *  Default: empty
     **/
    val engines: String? = null,

    /**
     * Code of the language.
     *
     * Parameter: optional
     *
     * Possible: TODO ...
     * Default: all (or empty for the call from the app)
     */
    val language: String? = null,

    // Search page number ->
    /**
     * Search page number.
     *
     * Parameter: optional
     *
     * Possible: 1, 2, ...
     * Default: 1
     */
    val pageNo: Int? = null,

    /**
     * Time range of search for engines which support it.
     * See if an engine supports time range search in the preferences page of an instance.
     *
     * Parameter: optional
     *
     * Possible: day, month, year
     * Default: empty
     */
    val timeRange: String? = null,

    /**
     * Output format of results.
     *
     * Parameter: optional
     *
     * Possible: json, csv, rss
     * Default: empty
     */
    val format: String? = null,

    /**
     * Proxy image results through searx.
     *
     * Parameter: optional
     *
     * Possible: True, False
     * Default: False
     */
    val imageProxy: String? = null,

    /**
     * Service which completes words as you type.
     *
     * Parameter: optional
     *
     * Possible: google, dbpedia, duckduckgo, startpage, wikipedia
     * Default: empty
     */
    val autoComplete: String? = null,

    /**
     * Filter search results of engines which support safe search.
     * See if an engine supports safe search in the preferences page of an instance.
     *
     * Parameter: optional
     *
     * Possible: 0, 1, None
     * Default: None
     */
    val safeSearch: String? = null
)