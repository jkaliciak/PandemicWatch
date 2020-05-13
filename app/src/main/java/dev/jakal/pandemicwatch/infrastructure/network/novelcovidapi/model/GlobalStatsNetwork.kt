package dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model

import com.squareup.moshi.JsonClass
import dev.jakal.pandemicwatch.common.utils.toLocalDateTime
import dev.jakal.pandemicwatch.domain.model.GlobalStats
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalStatsEntity

@JsonClass(generateAdapter = true)
data class GlobalStatsNetwork(
    val cases: Int,
    val todayCases: Int,
    val deaths: Int,
    val todayDeaths: Int,
    val recovered: Int,
    val active: Int,
    val critical: Int,
    val casesPerOneMillion: Int,
    val deathsPerOneMillion: Int,
    val updated: Long,
    val affectedCountries: Int,
    val tests: Long,
    val testsPerOneMillion: Double
)

fun GlobalStatsNetwork.toDomain() =
    GlobalStats(
        cases,
        todayCases,
        deaths,
        todayDeaths,
        recovered,
        active,
        critical,
        casesPerOneMillion,
        deathsPerOneMillion,
        updated.toLocalDateTime(),
        affectedCountries,
        tests,
        testsPerOneMillion
    )

fun GlobalStatsNetwork.toEntity() =
    GlobalStatsEntity(
        cases,
        todayCases,
        deaths,
        todayDeaths,
        recovered,
        active,
        critical,
        casesPerOneMillion,
        deathsPerOneMillion,
        updated.toLocalDateTime(),
        affectedCountries,
        tests,
        testsPerOneMillion
    )