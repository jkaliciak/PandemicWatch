package dev.jakal.pandemicwatch.infrastructure.database.model

import java.util.*

enum class TimelineType(val value: String) {
    CASE("case"),
    DEATH("death"),
    RECOVERY("recovery"),
    UNKNOWN("unknown");

    companion object {
        private val map = values().associateBy(TimelineType::value)

        fun fromString(type: String) = type.toLowerCase(Locale.getDefault())
            .let { map[it] ?: UNKNOWN }
    }
}