package dev.jakal.pandemicwatch.presentation.countrydetails

import org.threeten.bp.LocalDate

data class CountryHistoryPresentation(
    val casesHistory: Map<LocalDate, Int>,
    val deathsHistory: Map<LocalDate, Int>,
    val recoveredHistory: Map<LocalDate, Int>
)