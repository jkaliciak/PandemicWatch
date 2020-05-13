package dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model

import com.squareup.moshi.JsonClass
import dev.jakal.pandemicwatch.domain.model.CountryHistorical
import dev.jakal.pandemicwatch.infrastructure.database.model.CountryHistoricalEntity
import dev.jakal.pandemicwatch.infrastructure.database.model.TimelineEntity

@JsonClass(generateAdapter = true)
data class CountryHistoricalNetwork(
    val country: String,
    val timeline: TimelineNetwork
)

fun CountryHistoricalNetwork.toDomain() =
    CountryHistorical(
        country,
        timeline.toDomain()
    )

fun List<CountryHistoricalNetwork>.toDomain() = map { it.toDomain() }

fun CountryHistoricalNetwork.toEntity() = CountryHistoricalEntity(country)

fun List<CountryHistoricalNetwork>.toEntity() = map { it.toEntity() }

fun CountryHistoricalNetwork.toTimelineEntity(): List<TimelineEntity> = timeline.toEntity(country)

fun List<CountryHistoricalNetwork>.toTimelineEntity(): List<TimelineEntity> =
    flatMap { it.toTimelineEntity() }