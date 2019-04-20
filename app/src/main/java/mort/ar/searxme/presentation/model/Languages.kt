package mort.ar.searxme.presentation.model

enum class Languages(
    private val languageName: String,
    val languageParameter: String
) {
    DEFAULT_LANGUAGE("no specific language (default)", "default"),
    ENGLISH("English", "en"),
    GERMAN("German", "de"),
    FRENCH("French", "fr"),
    SPANISH("Spanish", "es");

    override fun toString() = languageName

}