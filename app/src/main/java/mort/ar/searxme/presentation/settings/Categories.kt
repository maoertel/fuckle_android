package mort.ar.searxme.presentation.settings

import mort.ar.searxme.R

enum class Categories(
    val urlParameter: String,
    val checkBox: Int
) {
    FILES("files", R.id.checkBoxFiles),
    IMAGES("images", R.id.checkBoxImages),
    IT("it", R.id.checkBoxIT),
    MAPS("maps", R.id.checkBoxMap),
    MUSIC("music", R.id.checkBoxMusic),
    NEWS("news", R.id.checkBoxNews),
    SCIENCE("science", R.id.checkBoxScience),
    SOCIAL_MEDIA("social_media", R.id.checkBoxSocialMedia),
    VIDEOS("videos", R.id.checkBoxVideo)
}