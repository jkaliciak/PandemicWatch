package dev.jakal.pandemicwatch.domain.model

data class CountryInfo(
    val iso2: String?,
    val iso3: String?,
    val lat: Double,
    val long: Double,
    val flagUrl: String
)