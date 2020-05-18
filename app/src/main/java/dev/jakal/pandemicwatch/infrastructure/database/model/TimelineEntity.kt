package dev.jakal.pandemicwatch.infrastructure.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.jakal.pandemicwatch.domain.model.Timeline
import org.threeten.bp.LocalDate

@Entity(tableName = "timeline")
data class TimelineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ForeignKey(
        entity = CountryHistoryEntity::class,
        parentColumns = ["country"],
        childColumns = ["country"],
        onDelete = ForeignKey.CASCADE
    )
    val country: String,
    val date: LocalDate,
    val amount: Int,
    val type: TimelineType
)

fun List<TimelineEntity>.toDomain() = Timeline(
    filter { it.type == TimelineType.CASE }.map { it.date to it.amount }.toMap(),
    filter { it.type == TimelineType.DEATH }.map { it.date to it.amount }.toMap(),
    filter { it.type == TimelineType.RECOVERY }.map { it.date to it.amount }.toMap()
)