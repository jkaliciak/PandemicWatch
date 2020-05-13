package dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model

import com.squareup.moshi.JsonClass
import dev.jakal.pandemicwatch.domain.model.CountryInfo
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryInfoEmbedded

@JsonClass(generateAdapter = true)
data class CountryInfoNetwork(
    val _id: Int?,
    val iso2: String?,
    val iso3: String?,
    val lat: Double,
    val long: Double,
    val flag: String
)

fun CountryInfoNetwork.toDomain() =
    CountryInfo(
        iso2,
        iso3,
        lat,
        long,
        flag
    )

fun CountryInfoNetwork.toEntity() =
    CountryInfoEmbedded(
        iso2,
        iso3,
        lat,
        long,
        flag
    )