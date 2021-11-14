package org.cron.substitutes

enum class FieldType(
    val possibleValues: IntRange,
    val displayName: String
) {
    MINUTE(0..59, "minute"),
    HOUR(0..23, "hour"),
    DAY_OF_MONTH(1..31, "day of month"),
    MONTH(1..12, "month"),
    DAY_OF_WEEK(0..6, "day of week")
}