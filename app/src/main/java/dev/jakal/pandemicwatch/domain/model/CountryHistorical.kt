package dev.jakal.pandemicwatch.domain.model

import dev.jakal.pandemicwatch.presentation.countrydetails.CountryHistoricalPresentation

data class CountryHistorical(
    val country : String,
    val timeline : Timeline
)

fun CountryHistorical.toPresentation() = CountryHistoricalPresentation(
    casesHistory = timeline.cases,
    deathsHistory = timeline.deaths,
    recoveredHistory = timeline.recovered
)