package dev.jakal.pandemicwatch.domain.model

import dev.jakal.pandemicwatch.presentation.countrydetails.CountryHistoryPresentation

data class CountryHistory(
    val country : String,
    val timeline : Timeline
)

fun CountryHistory.toPresentation() = CountryHistoryPresentation(
    casesHistory = timeline.cases,
    deathsHistory = timeline.deaths,
    recoveredHistory = timeline.recovered
)