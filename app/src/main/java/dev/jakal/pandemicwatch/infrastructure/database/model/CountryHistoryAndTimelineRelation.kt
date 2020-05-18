package dev.jakal.pandemicwatch.infrastructure.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.jakal.pandemicwatch.domain.model.CountryHistory

data class CountryHistoryAndTimelineRelation(
    @Embedded val historyEntity: CountryHistoryEntity,
    @Relation(
        parentColumn = "country",
        entityColumn = "country"
    )
    val timelineEntities: List<TimelineEntity>
)

fun CountryHistoryAndTimelineRelation.toDomain() = CountryHistory(
    historyEntity.country,
    timelineEntities.toDomain()
)

fun List<CountryHistoryAndTimelineRelation>.toDomain() = map { it.toDomain() }