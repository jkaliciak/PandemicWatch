package dev.jakal.pandemicwatch.infrastructure.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.jakal.pandemicwatch.infrastructure.database.converter.LocalDateConverter
import dev.jakal.pandemicwatch.infrastructure.database.converter.LocalDateTimeConverter
import dev.jakal.pandemicwatch.infrastructure.database.converter.TimelineTypeConverter
import dev.jakal.pandemicwatch.infrastructure.database.dao.CountryDao
import dev.jakal.pandemicwatch.infrastructure.database.dao.CountryHistoryDao
import dev.jakal.pandemicwatch.infrastructure.database.dao.TimelineDao
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryEntity
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryHistoryEntity
import dev.jakal.pandemicwatch.infrastructure.database.model.TimelineEntity

@Database(
    entities = [
        CountryEntity::class,
        CountryHistoryEntity::class,
        TimelineEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    LocalDateConverter::class,
    LocalDateTimeConverter::class,
    TimelineTypeConverter::class
)
abstract class CovidDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao

    abstract fun countryHistoryDao(): CountryHistoryDao

    abstract fun timelineDao(): TimelineDao
}