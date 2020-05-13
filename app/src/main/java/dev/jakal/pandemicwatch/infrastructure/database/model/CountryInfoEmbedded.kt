package dev.jakal.pandemicwatch.infrastructure.database.model

import dev.jakal.pandemicwatch.domain.model.CountryInfo

data class CountryInfoEmbedded(
    var iso2: String?,
    var iso3: String?,
    var lat: Double,
    var long: Double,
    var flag: String
) {
    constructor() : this(null, null, 0.0, 0.0, "")
}

fun CountryInfoEmbedded.toDomain() =
    CountryInfo(
        iso2,
        iso3,
        lat,
        long,
        flag
    )