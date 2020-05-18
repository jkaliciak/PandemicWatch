package dev.jakal.pandemicwatch.domain.model

import dev.jakal.pandemicwatch.presentation.overview.GlobalHistoryPresentation
import org.threeten.bp.LocalDate

data class GlobalHistory(
    val cases: Map<LocalDate, Int>,
    val deaths: Map<LocalDate, Int>,
    val recovered: Map<LocalDate, Int>
)

fun GlobalHistory.toPresentation() = GlobalHistoryPresentation(
    casesHistory = cases,
    deathsHistory = deaths,
    recoveredHistory = recovered
)