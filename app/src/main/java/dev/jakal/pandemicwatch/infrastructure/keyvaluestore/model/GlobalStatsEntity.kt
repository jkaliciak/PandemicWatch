package dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model

import com.squareup.moshi.JsonClass
import dev.jakal.pandemicwatch.domain.model.GlobalStats
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class GlobalStatsEntity(
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
    val affectedCountries: Int,
    val tests: Long,
    val testsPerOneMillion: Double
)

fun GlobalStatsEntity.toDomain() = GlobalStats(
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
    affectedCountries,
    tests,
    testsPerOneMillion
)