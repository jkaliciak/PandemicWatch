package dev.jakal.pandemicwatch.presentation.overview

import org.threeten.bp.LocalDate

data class GlobalHistoricalPresentation(
    val casesHistory: Map<LocalDate, Int>,
    val deathsHistory: Map<LocalDate, Int>,
    val recoveredHistory: Map<LocalDate, Int>
)