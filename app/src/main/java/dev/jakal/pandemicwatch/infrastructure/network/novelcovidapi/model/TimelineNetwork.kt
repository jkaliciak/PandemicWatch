package dev.jakal.pandemicwatch.infrastructure.network.novelcovidapi.model

import com.squareup.moshi.JsonClass
import dev.jakal.pandemicwatch.common.utils.toLocalDateFromNovelCovidAPIDate
import dev.jakal.pandemicwatch.domain.model.GlobalHistory
import dev.jakal.pandemicwatch.domain.model.Timeline
import dev.jakal.pandemicwatch.infrastructure.database.model.TimelineEntity
import dev.jakal.pandemicwatch.infrastructure.database.model.TimelineType
import dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model.GlobalHistoryEntity

@JsonClass(generateAdapter = true)
data class TimelineNetwork(
    val cases: Map<String, Int>,
    val deaths: Map<String, Int>,
    val recovered: Map<String, Int>
)

fun TimelineNetwork.toDomain() =
    Timeline(
        cases = cases.map { it.key.toLocalDateFromNovelCovidAPIDate() to it.value }.toMap(),
        deaths = deaths.map { it.key.toLocalDateFromNovelCovidAPIDate() to it.value }.toMap(),
        recovered = recovered.map { it.key.toLocalDateFromNovelCovidAPIDate() to it.value }.toMap()
    )

fun TimelineNetwork.toEntity(country: String) = mutableListOf<TimelineEntity>().apply {
    addAll(cases.map { it.toTimelineEntity(country, TimelineType.CASE) })
    addAll(deaths.map { it.toTimelineEntity(country, TimelineType.DEATH) })
    addAll(recovered.map { it.toTimelineEntity(country, TimelineType.RECOVERY) })
}

private fun Map.Entry<String, Int>.toTimelineEntity(country: String, timelineType: TimelineType) =
    TimelineEntity(
        0,
        country,
        key.toLocalDateFromNovelCovidAPIDate(),
        value,
        timelineType
    )

fun TimelineNetwork.toGlobalHistoryEntity() =
    GlobalHistoryEntity(
        cases = cases,
        deaths = deaths,
        recovered = recovered
    )

fun TimelineNetwork.toGlobalHistoryDomain() =
    GlobalHistory(
        cases = cases.map { it.key.toLocalDateFromNovelCovidAPIDate() to it.value }.toMap(),
        deaths = deaths.map { it.key.toLocalDateFromNovelCovidAPIDate() to it.value }.toMap(),
        recovered = recovered.map { it.key.toLocalDateFromNovelCovidAPIDate() to it.value }.toMap()
    )