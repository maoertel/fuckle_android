package mort.ar.searxme.model


enum class Languages(
    val languageName: String,
    val languageParameter: String?
) {
    DEFAULT("no specific language (default)", null),
    ENGLISH("English", "en"),
    GERMAN("German", "de"),
    FRENCH("French", "fr"),
    SPANISH("Spanish", "es");

    override fun toString(): String {
        return languageName
    }

}
