package dev.jakal.pandemicwatch.domain.model

import dev.jakal.pandemicwatch.common.utils.formatDecimalPoints
import dev.jakal.pandemicwatch.common.utils.toDateTimeFormat
import dev.jakal.pandemicwatch.presentation.overview.GlobalStatsPresentation
import org.threeten.bp.LocalDateTime

data class GlobalStats(
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

fun GlobalStats.toPresentation() = GlobalStatsPresentation(
    cases.toString(),
    todayCases.toString(),
    deaths.toString(),
    todayDeaths.toString(),
    recovered.toString(),
    active.toString(),
    critical.toString(),
    casesPerOneMillion.formatDecimalPoints(1),
    deathsPerOneMillion.formatDecimalPoints(1),
    updated.toDateTimeFormat(),
    affectedCountries.toString(),
    tests.toString(),
    testsPerOneMillion.formatDecimalPoints(1)
)