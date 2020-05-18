package dev.jakal.pandemicwatch.infrastructure.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_history")
data class CountryHistoryEntity(
    @PrimaryKey(autoGenerate = false) val country: String
)