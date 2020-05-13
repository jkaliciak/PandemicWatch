package dev.jakal.pandemicwatch.infrastructure.database.converter

import androidx.room.TypeConverter
import dev.jakal.pandemicwatch.infrastructure.database.model.TimelineType


class TimelineTypeConverter {

    @TypeConverter
    fun fromString(value: String): TimelineType = TimelineType.fromString(value)

    @TypeConverter
    fun toString(timelineType: TimelineType): String = timelineType.value
}
