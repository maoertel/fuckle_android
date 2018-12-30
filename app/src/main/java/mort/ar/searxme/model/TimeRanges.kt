package mort.ar.searxme.model


enum class TimeRanges(
    val rangeName: String,
    val rangeParameter: String?
) {
    DEFAULT("none (default)", null),
    DAY("Last day", "day"),
    WEEK("Last week", "week"),
    MONTH("Last month", "month"),
    YEAR("Last year", "year");

    override fun toString(): String {
        return rangeName
    }

}