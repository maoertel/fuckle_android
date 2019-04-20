package mort.ar.searxme.presentation.settings

import mort.ar.searxme.R

enum class Engines(
    val urlParameter: String,
    val checkBox: Int
) {
    DUCKDUCKGO("duckduckgo", R.id.checkBoxDdg),
    WIKIPEDIA("wikipedia", R.id.checkBoxWikipedia),
    DUDEN("duden", R.id.checkBoxDuden),
    BING("bing", R.id.checkBoxBing),
    GOOGLE("google", R.id.checkBoxGoogle),
    QWANT("qwant", R.id.checkBoxQwant)
}