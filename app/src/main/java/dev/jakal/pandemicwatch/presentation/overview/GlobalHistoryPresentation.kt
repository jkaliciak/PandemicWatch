package dev.jakal.pandemicwatch.presentation.overview

import org.threeten.bp.LocalDate

data class GlobalHistoryPresentation(
    val casesHistory: Map<LocalDate, Int>,
    val deathsHistory: Map<LocalDate, Int>,
    val recoveredHistory: Map<LocalDate, Int>
)