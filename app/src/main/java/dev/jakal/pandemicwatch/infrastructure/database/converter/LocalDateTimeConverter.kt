package dev.jakal.pandemicwatch.infrastructure.database.converter

import androidx.room.TypeConverter
import dev.jakal.pandemicwatch.common.utils.toLocalDateTime
import dev.jakal.pandemicwatch.common.utils.toLong
import org.threeten.bp.LocalDateTime


class LocalDateTimeConverter {

    @TypeConverter
    fun toLocalDateTime(value: Long): LocalDateTime = value.toLocalDateTime()

    @TypeConverter
    fun toLong(localDateTime: LocalDateTime): Long = localDateTime.toLong()
}
