package dev.jakal.pandemicwatch.infrastructure.database.converter

import androidx.room.TypeConverter
import dev.jakal.pandemicwatch.common.utils.toLocalDate
import dev.jakal.pandemicwatch.common.utils.toLong
import org.threeten.bp.LocalDate


class LocalDateConverter {

    @TypeConverter
    fun toLocalDate(value: Long): LocalDate = value.toLocalDate()

    @TypeConverter
    fun toLong(localDate: LocalDate): Long = localDate.toLong()
}
