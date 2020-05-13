package dev.jakal.pandemicwatch.infrastructure.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.jakal.pandemicwatch.domain.model.CountryHistorical

data class CountryHistoricalAndTimelineRelation(
    @Embedded val historicalEntity: CountryHistoricalEntity,
    @Relation(
        parentColumn = "country",
        entityColumn = "country"
    )
    val timelineEntities: List<TimelineEntity>
)

fun CountryHistoricalAndTimelineRelation.toDomain() = CountryHistorical(
    historicalEntity.country,
    timelineEntities.toDomain()
)

fun List<CountryHistoricalAndTimelineRelation>.toDomain() = map { it.toDomain() }