package dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model

import com.squareup.moshi.JsonClass
import dev.jakal.pandemicwatch.common.utils.toLocalDateTime
import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryEntity

@JsonClass(generateAdapter = true)
data class CountryNetwork(
    val country: String,
    val countryInfo: CountryInfoNetwork,
    val cases: Int,
    val todayCases: Int,
    val deaths: Int,
    val todayDeaths: Int,
    val recovered: Int,
    val active: Int,
    val critical: Int,
    val casesPerOneMillion: Double,
    val deathsPerOneMillion: Double,
    val updated: Long,
    val tests: Long,
    val testsPerOneMillion: Double,
    val continent: String
)

fun CountryNetwork.toDomain() =
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
        updated.toLocalDateTime(),
        tests,
        testsPerOneMillion,
        continent,
        false
    )

fun List<CountryNetwork>.toDomain() = map { it.toDomain() }

fun CountryNetwork.toEntity() =
    CountryEntity(
        country,
        countryInfo.toEntity(),
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
        tests,
        testsPerOneMillion,
        continent
    )

fun List<CountryNetwork>.toEntity() = map { it.toEntity() }