package dev.jakal.pandemicwatch.infrastructure.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_historical")
data class CountryHistoricalEntity(
    @PrimaryKey(autoGenerate = false) val country: String
)