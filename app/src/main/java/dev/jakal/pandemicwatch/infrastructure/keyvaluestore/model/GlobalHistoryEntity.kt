package dev.jakal.pandemicwatch.infrastructure.keyvaluestore.model

import com.squareup.moshi.JsonClass
import dev.jakal.pandemicwatch.common.utils.toLocalDateFromNovelCovidAPIDate
import dev.jakal.pandemicwatch.domain.model.GlobalHistory

@JsonClass(generateAdapter = true)
data class GlobalHistoryEntity(
    val cases: Map<String, Int>,
    val deaths: Map<String, Int>,
    val recovered: Map<String, Int>
)

fun GlobalHistoryEntity.toDomain() = GlobalHistory(
    cases = cases.map { it.key.toLocalDateFromNovelCovidAPIDate() to it.value }.toMap(),
    deaths = deaths.map { it.key.toLocalDateFromNovelCovidAPIDate() to it.value }.toMap(),
    recovered = recovered.map { it.key.toLocalDateFromNovelCovidAPIDate() to it.value }.toMap()
)