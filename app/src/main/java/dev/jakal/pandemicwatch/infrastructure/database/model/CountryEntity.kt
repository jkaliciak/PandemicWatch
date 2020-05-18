package dev.jakal.pandemicwatch.infrastructure.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jakal.pandemicwatch.domain.model.Country
import org.threeten.bp.LocalDateTime

@Entity(tableName = "country")
data class CountryEntity(
    @PrimaryKey(autoGenerate = false) val country: String,
    @Embedded val countryInfo: CountryInfoEmbedded,
    val cases: Int,
    val todayCases: Int,
    val deaths: Int,
    val todayDeaths: Int,
    val recovered: Int,
    val active: Int,
    val critical: Int,
    val casesPerOneMillion: Double,
    val deathsPerOneMillion: Double,
    val updated: LocalDateTime,
    val tests: Long,
    val testsPerOneMillion: Double,
    val continent: String
)

fun CountryEntity.toDomain(favorite: Boolean = false) =
    Country(
        country,
        countryInfo.toDomain(),
        cases,
        todayCases,
        deaths,
        todayDeaths,
        recovered,
        active,
        critical,
        casesPerOneMillion,
        deathsPerOneMillion,
        updated,
        tests,
        testsPerOneMillion,
        continent,
        favorite = favorite
    )

fun List<CountryEntity>.toDomain(favoriteCountries: Set<String> = emptySet()) =
    map { it.toDomain(favoriteCountries.contains(it.country)) }