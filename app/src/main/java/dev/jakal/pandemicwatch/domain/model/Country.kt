package dev.jakal.pandemicwatch.domain.model

import dev.jakal.pandemicwatch.common.utils.toDateTimeFormat
import dev.jakal.pandemicwatch.presentation.countrydetails.CountryPresentation
import dev.jakal.pandemicwatch.presentation.countrylist.CountryListPresentation
import org.threeten.bp.LocalDateTime

data class Country(
    val country: String,
    val countryInfo: CountryInfo,
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
    val continent: String,
    val favorite: Boolean
)

fun List<Country>.toPresentation() = CountryListPresentation(
    this
)

fun Country.toPresentation() = CountryPresentation(
    cases = cases.toString(),
    todayCases = todayCases.toString(),
    deaths = deaths.toString(),
    todayDeaths = todayDeaths.toString(),
    recovered = recovered.toString(),
    active = active.toString(),
    critical = critical.toString(),
    casesPerOneMillion = casesPerOneMillion.toString(),
    deathsPerOneMillion = deathsPerOneMillion.toString(),
    updated = updated.toDateTimeFormat(),
    tests = tests.toString(),
    testsPerOneMillion = testsPerOneMillion.toString(),
    country = country,
    continent = continent,
    lat = countryInfo.lat,
    long = countryInfo.long,
    flagUrl = countryInfo.flagUrl,
    favorite = favorite
)