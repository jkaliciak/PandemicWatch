package dev.jakal.pandemicwatch.domain.model

import dev.jakal.pandemicwatch.presentation.overview.GlobalHistoricalPresentation
import org.threeten.bp.LocalDate

data class GlobalHistorical(
    val cases: Map<LocalDate, Int>,
    val deaths: Map<LocalDate, Int>,
    val recovered: Map<LocalDate, Int>
)

fun GlobalHistorical.toPresentation() = GlobalHistoricalPresentation(
    casesHistory = cases,
    deathsHistory = deaths,
    recoveredHistory = recovered
)